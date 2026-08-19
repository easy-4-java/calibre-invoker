package io.github.easy4j.calibre.invoker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.junit.Test;

public class PublicApiBaselineTest
{

    private static final Set<String> APPROVED_ADDITIVE_ABSTRACT_METHOD_SIGNATURES = new HashSet<>( Arrays.asList(
            "addAllowedPlugin(String):InvocationRequest",
            "setAllowedPlugins(List):InvocationRequest",
            "setAuthors(String):InvocationRequest",
            "setIsbn(String):InvocationRequest",
            "setTitle(String):InvocationRequest" ) );

    private static final String FETCH_EBOOK_METADATA_REQUEST =
            "io.github.easy4j.calibre.invoker.request.FetchEbookMetadataInvocationRequest";

    private static final String INVOCATION_OUTPUT_HANDLER =
            "io.github.easy4j.calibre.invoker.InvocationOutputHandler";

    private static final String INVOCATION_REQUEST =
            "io.github.easy4j.calibre.invoker.request.InvocationRequest";

    private static final Map<String, Set<String>> BASELINE_INTERFACE_PARENTS_BY_OWNER =
            baselineInterfaceParentsByOwner();

    private static final Map<String, Set<String>> BASELINE_EXTERNAL_ABSTRACT_METHODS_BY_OWNER =
            baselineExternalAbstractMethodsByOwner();

    @Test
    public void publicTypesMatchBaseline() throws Exception
    {
        List<String> expected = Files.readAllLines(
                Paths.get( "src/test/resources/api/calibre-invoker-2.0-public-types.txt" ),
                StandardCharsets.UTF_8 );
        List<String> actual = PublicApiScanner.scan( "io.github.easy4j.calibre.invoker" );
        List<String> sortedExpected = new ArrayList<>( expected );
        Collections.sort( sortedExpected );
        assertEquals( sortedExpected, expected );
        assertEquals( new TreeSet<>( expected ).size(), expected.size() );
        assertTrue( actual.containsAll( expected ) );
    }

    @Test
    public void existingPublicInterfacesHaveNoUnapprovedAbstractMethodAdditions() throws Exception
    {
        List<String> baseline = Files.readAllLines(
                Paths.get( "src/test/resources/api/calibre-invoker-2.0-public-types.txt" ),
                StandardCharsets.UTF_8 );
        Map<String, Set<String>> approvedAdditionsByOwner = new HashMap<>();
        approvedAdditionsByOwner.put( FETCH_EBOOK_METADATA_REQUEST,
                APPROVED_ADDITIVE_ABSTRACT_METHOD_SIGNATURES );

        assertEquals( Collections.emptyList(), PublicApiScanner.findUnapprovedAbstractInterfaceMethods(
                "io.github.easy4j.calibre.invoker", baseline,
                BASELINE_INTERFACE_PARENTS_BY_OWNER,
                BASELINE_EXTERNAL_ABSTRACT_METHODS_BY_OWNER,
                approvedAdditionsByOwner ) );
    }

    @Test
    public void directAbstractAdditionIsRejected()
    {
        assertEquals( Collections.singletonList(
                DirectAbstractAdditionContract.class.getName() + "#directAddition():void" ),
                scanFixture( DirectAbstractAdditionContract.class,
                        signatures( "existing():void" ) ) );
    }

    @Test
    public void inheritedAbstractAdditionFromNewParentIsRejected()
    {
        assertEquals( Collections.singletonList(
                InheritedAbstractAdditionContract.class.getName() + "#inheritedAddition():void" ),
                scanFixture( InheritedAbstractAdditionContract.class,
                        signatures( "existing():void" ) ) );
    }

    @Test
    public void defaultAndStaticAdditionsAreAllowed()
    {
        assertEquals( Collections.emptyList(), scanFixture( DefaultAndStaticAdditionContract.class,
                signatures( "existing():void" ) ) );
    }

    @Test
    public void independentNewPublicInterfaceIsAllowed()
    {
        assertEquals( Collections.emptyList(), PublicApiScanner.findUnapprovedAbstractInterfaceMethods(
                Arrays.asList( IndependentNewContract.class ), Collections.emptyMap() ) );
    }

    @Test
    public void exactApprovedAbstractAdditionIsAllowed()
    {
        assertEquals( Collections.emptyList(), scanFixture( ExactApprovedAdditionContract.class,
                signatures( "existing():void", "approved(String):void" ) ) );
    }

    @Test
    public void nonApprovedSignatureIsRejected()
    {
        assertEquals( Collections.singletonList(
                NonApprovedSignatureContract.class.getName() + "#approved(int):void" ),
                scanFixture( NonApprovedSignatureContract.class,
                        signatures( "existing():void", "approved(String):void" ) ) );
    }

    @Test
    public void approvalForOneInterfaceDoesNotApproveAnotherInterface()
    {
        Map<String, Set<String>> approvedMethodsByOwner = new HashMap<>();
        approvedMethodsByOwner.put( ApprovedOwnerContract.class.getName(),
                signatures( "existing():void", "approved(String):void" ) );
        approvedMethodsByOwner.put( CrossOwnerContract.class.getName(),
                signatures( "existing():void" ) );

        assertEquals( Collections.singletonList(
                CrossOwnerContract.class.getName() + "#approved(String):void" ),
                PublicApiScanner.findUnapprovedAbstractInterfaceMethods(
                        Arrays.asList( ApprovedOwnerContract.class, CrossOwnerContract.class ),
                        approvedMethodsByOwner ) );
    }

    @Test
    public void baselineClassMethodsDoNotApproveInterfaceMethods()
    {
        List<String> baseline = Arrays.asList(
                "CLASS " + BaselineClassContract.class.getName(),
                "METHOD " + BaselineClassContract.class.getName() + "#approved(String):void",
                "CLASS " + ClassSignatureCollisionContract.class.getName(),
                "METHOD " + ClassSignatureCollisionContract.class.getName() + "#existing():void" );
        Map<String, Set<String>> approvedMethodsByOwner =
                PublicApiScanner.effectiveApprovedAbstractMethodsByOwner(
                        Arrays.asList( BaselineClassContract.class, ClassSignatureCollisionContract.class ),
                        baseline, Collections.emptyMap(), Collections.emptyMap(),
                        Collections.emptyMap() );

        assertEquals( Collections.singletonList(
                ClassSignatureCollisionContract.class.getName() + "#approved(String):void" ),
                PublicApiScanner.findUnapprovedAbstractInterfaceMethods(
                        Arrays.asList( BaselineClassContract.class, ClassSignatureCollisionContract.class ),
                        approvedMethodsByOwner ) );
    }

    @Test
    public void currentEdgeBetweenBaselineInterfacesDoesNotRewriteHistory()
    {
        List<String> baseline = Arrays.asList(
                "CLASS " + BaselineParentContract.class.getName(),
                "METHOD " + BaselineParentContract.class.getName() + "#inheritedAddition():void",
                "CLASS " + BaselineChildWithNewEdgeContract.class.getName(),
                "METHOD " + BaselineChildWithNewEdgeContract.class.getName() + "#existing():void" );
        Map<String, Set<String>> approvedMethodsByOwner =
                PublicApiScanner.effectiveApprovedAbstractMethodsByOwner(
                        Arrays.asList( BaselineParentContract.class, BaselineChildWithNewEdgeContract.class ),
                        baseline, Collections.emptyMap(), Collections.emptyMap(),
                        Collections.emptyMap() );

        assertEquals( Collections.singletonList(
                BaselineChildWithNewEdgeContract.class.getName() + "#inheritedAddition():void" ),
                PublicApiScanner.findUnapprovedAbstractInterfaceMethods(
                        Arrays.asList( BaselineParentContract.class, BaselineChildWithNewEdgeContract.class ),
                        approvedMethodsByOwner ) );
    }

    private List<String> scanFixture( Class<?> baselineType, Set<String> approvedSignatures )
    {
        Map<String, Set<String>> approvedMethodsByOwner = new HashMap<>();
        approvedMethodsByOwner.put( baselineType.getName(), approvedSignatures );
        return PublicApiScanner.findUnapprovedAbstractInterfaceMethods(
                Arrays.asList( baselineType, IndependentNewContract.class ),
                approvedMethodsByOwner );
    }

    private Set<String> signatures( String... signatures )
    {
        return new HashSet<>( Arrays.asList( signatures ) );
    }

    private static Map<String, Set<String>> baselineInterfaceParentsByOwner()
    {
        // 固化自 c3053d5 基线；不得使用当前反射继承关系重建历史边。
        Map<String, Set<String>> parentsByOwner = new HashMap<>();
        parentsByOwner.put( FETCH_EBOOK_METADATA_REQUEST, Collections.singleton( INVOCATION_REQUEST ) );
        parentsByOwner.put( "io.github.easy4j.calibre.invoker.request.Lrf2lrsInvocationRequest",
                Collections.singleton( INVOCATION_REQUEST ) );
        parentsByOwner.put( "io.github.easy4j.calibre.invoker.request.LrfviewerInvocationRequest",
                Collections.singleton( INVOCATION_REQUEST ) );
        parentsByOwner.put( "io.github.easy4j.calibre.invoker.request.Lrs2lrfInvocationRequest",
                Collections.singleton( INVOCATION_REQUEST ) );
        parentsByOwner.put( "io.github.easy4j.calibre.invoker.request.Web2diskInvocationRequest",
                Collections.singleton( INVOCATION_REQUEST ) );
        return Collections.unmodifiableMap( parentsByOwner );
    }

    private static Map<String, Set<String>> baselineExternalAbstractMethodsByOwner()
    {
        // owner#method 基线只记录声明方法，外部父接口的历史契约需单独固化。
        Map<String, Set<String>> methodsByOwner = new HashMap<>();
        methodsByOwner.put( INVOCATION_OUTPUT_HANDLER,
                Collections.singleton( "consumeLine(String):void" ) );
        return Collections.unmodifiableMap( methodsByOwner );
    }

    static final class PublicApiScanner
    {

        static List<String> scan( String packageName ) throws Exception
        {
            Path classesDirectory = Paths.get( Invoker.class.getProtectionDomain().getCodeSource().getLocation().toURI() );
            Path packageDirectory = classesDirectory.resolve( packageName.replace( '.', '/' ) );
            TreeSet<String> descriptors = new TreeSet<>();
            try ( Stream<Path> paths = Files.walk( packageDirectory ) )
            {
                paths.filter( Files::isRegularFile )
                        .filter( path -> path.toString().endsWith( ".class" ) )
                        .map( packageDirectory::relativize )
                        .map( path -> toClassName( packageName, path ) )
                        .forEach( className -> addPublicDescriptors( descriptors, className ) );
            }
            return new ArrayList<>( descriptors );
        }

        static List<String> findUnapprovedAbstractInterfaceMethods( String packageName,
                Iterable<String> baselineDescriptors,
                Map<String, Set<String>> baselineParentsByOwner,
                Map<String, Set<String>> baselineExternalMethodsByOwner,
                Map<String, Set<String>> approvedAdditionsByOwner ) throws Exception
        {
            Path classesDirectory = Paths.get( Invoker.class.getProtectionDomain().getCodeSource().getLocation().toURI() );
            Path packageDirectory = classesDirectory.resolve( packageName.replace( '.', '/' ) );
            List<Class<?>> publicTypes = new ArrayList<>();
            try ( Stream<Path> paths = Files.walk( packageDirectory ) )
            {
                paths.filter( Files::isRegularFile )
                        .filter( path -> path.toString().endsWith( ".class" ) )
                        .map( packageDirectory::relativize )
                        .map( path -> toClassName( packageName, path ) )
                        .map( PublicApiScanner::loadPublicType )
                        .filter( Objects::nonNull )
                        .forEach( publicTypes::add );
            }
            Map<String, Set<String>> approvedMethodsByOwner = effectiveApprovedAbstractMethodsByOwner(
                    publicTypes, baselineDescriptors, baselineParentsByOwner,
                    baselineExternalMethodsByOwner, approvedAdditionsByOwner );
            return findUnapprovedAbstractInterfaceMethods( publicTypes, approvedMethodsByOwner );
        }

        static List<String> findUnapprovedAbstractInterfaceMethods( Iterable<Class<?>> publicTypes,
                Map<String, Set<String>> approvedMethodSignaturesByOwner )
        {
            TreeSet<String> violations = new TreeSet<>();
            for ( Class<?> type : publicTypes )
            {
                if ( !Modifier.isPublic( type.getModifiers() ) || !type.isInterface()
                        || !approvedMethodSignaturesByOwner.containsKey( type.getName() ) )
                {
                    continue;
                }
                Set<String> approvedMethodSignatures = approvedMethodSignaturesByOwner.get( type.getName() );
                for ( Method method : type.getMethods() )
                {
                    if ( Modifier.isPublic( method.getModifiers() )
                            && Modifier.isAbstract( method.getModifiers() )
                            && !method.isBridge() && !method.isSynthetic() )
                    {
                        String signature = methodSignature( method );
                        if ( !approvedMethodSignatures.contains( signature ) )
                        {
                            violations.add( type.getName() + "#" + signature );
                        }
                    }
                }
            }
            return new ArrayList<>( violations );
        }

        static Map<String, Set<String>> effectiveApprovedAbstractMethodsByOwner(
                Iterable<Class<?>> publicTypes, Iterable<String> baselineDescriptors,
                Map<String, Set<String>> baselineParentsByOwner,
                Map<String, Set<String>> baselineExternalMethodsByOwner,
                Map<String, Set<String>> approvedAdditionsByOwner )
        {
            Map<String, Class<?>> publicTypesByName = new HashMap<>();
            for ( Class<?> type : publicTypes )
            {
                publicTypesByName.put( type.getName(), type );
            }

            Set<String> baselineTypeNames = classNames( baselineDescriptors );
            Map<String, Set<String>> declaredBaselineMethodsByInterface = new HashMap<>();
            for ( String descriptor : baselineDescriptors )
            {
                if ( !descriptor.startsWith( "METHOD " ) )
                {
                    continue;
                }
                int ownerEnd = descriptor.indexOf( '#' );
                String ownerName = descriptor.substring( "METHOD ".length(), ownerEnd );
                Class<?> ownerType = publicTypesByName.get( ownerName );
                if ( Objects.nonNull( ownerType ) && ownerType.isInterface()
                        && baselineTypeNames.contains( ownerName ) )
                {
                    declaredBaselineMethodsByInterface
                            .computeIfAbsent( ownerName, ignored -> new HashSet<>() )
                            .add( descriptor.substring( ownerEnd + 1 ) );
                }
            }
            for ( Map.Entry<String, Set<String>> entry : baselineExternalMethodsByOwner.entrySet() )
            {
                if ( baselineTypeNames.contains( entry.getKey() ) )
                {
                    declaredBaselineMethodsByInterface
                            .computeIfAbsent( entry.getKey(), ignored -> new HashSet<>() )
                            .addAll( entry.getValue() );
                }
            }

            Map<String, Set<String>> effectiveMethodsByOwner = new HashMap<>();
            for ( String baselineTypeName : baselineTypeNames )
            {
                Class<?> baselineType = publicTypesByName.get( baselineTypeName );
                if ( Objects.isNull( baselineType ) || !baselineType.isInterface() )
                {
                    continue;
                }
                Set<String> effectiveMethods = new HashSet<>();
                collectHistoricalAbstractMethods( baselineTypeName,
                        declaredBaselineMethodsByInterface, baselineParentsByOwner,
                        effectiveMethods, new HashSet<>() );
                effectiveMethods.addAll( approvedAdditionsByOwner.getOrDefault(
                        baselineTypeName, Collections.emptySet() ) );
                effectiveMethodsByOwner.put( baselineTypeName, effectiveMethods );
            }
            return effectiveMethodsByOwner;
        }

        private static void collectHistoricalAbstractMethods( String interfaceName,
                Map<String, Set<String>> declaredMethodsByInterface,
                Map<String, Set<String>> baselineParentsByOwner,
                Set<String> effectiveMethods, Set<String> visitedInterfaces )
        {
            if ( !visitedInterfaces.add( interfaceName ) )
            {
                return;
            }
            effectiveMethods.addAll( declaredMethodsByInterface.getOrDefault(
                    interfaceName, Collections.emptySet() ) );
            for ( String parentName : baselineParentsByOwner.getOrDefault(
                    interfaceName, Collections.emptySet() ) )
            {
                collectHistoricalAbstractMethods( parentName, declaredMethodsByInterface,
                        baselineParentsByOwner, effectiveMethods, visitedInterfaces );
            }
        }

        static Set<String> classNames( Iterable<String> descriptors )
        {
            Set<String> classNames = new HashSet<>();
            for ( String descriptor : descriptors )
            {
                if ( descriptor.startsWith( "CLASS " ) )
                {
                    classNames.add( descriptor.substring( "CLASS ".length() ) );
                }
            }
            return classNames;
        }

        private static Class<?> loadPublicType( String className )
        {
            try
            {
                Class<?> type = Class.forName( className, false, Invoker.class.getClassLoader() );
                return Modifier.isPublic( type.getModifiers() ) ? type : null;
            }
            catch ( ClassNotFoundException exception )
            {
                throw new IllegalStateException( "Could not load public API type " + className, exception );
            }
        }

        private static String toClassName( String packageName, Path relativeClassFile )
        {
            String relativeName = relativeClassFile.toString().replace( relativeClassFile.getFileSystem().getSeparator(), "." );
            return packageName + "." + relativeName.substring( 0, relativeName.length() - ".class".length() );
        }

        private static void addPublicDescriptors( TreeSet<String> descriptors, String className )
        {
            try
            {
                Class<?> type = Class.forName( className, false, Invoker.class.getClassLoader() );
                if ( !Modifier.isPublic( type.getModifiers() ) )
                {
                    return;
                }
                descriptors.add( "CLASS " + type.getName() );
                for ( Method method : type.getDeclaredMethods() )
                {
                    if ( Modifier.isPublic( method.getModifiers() ) && !method.isBridge() && !method.isSynthetic() )
                    {
                        descriptors.add( methodDescriptor( type, method ) );
                    }
                }
            }
            catch ( ClassNotFoundException exception )
            {
                throw new IllegalStateException( "Could not load public API type " + className, exception );
            }
        }

        private static String methodDescriptor( Class<?> type, Method method )
        {
            return "METHOD " + type.getName() + "#" + methodSignature( method );
        }

        private static String methodSignature( Method method )
        {
            List<String> parameterTypes = new ArrayList<>();
            for ( Class<?> parameterType : method.getParameterTypes() )
            {
                parameterTypes.add( parameterType.getSimpleName() );
            }
            return method.getName() + "(" + String.join( ",", parameterTypes ) + "):"
                    + method.getReturnType().getSimpleName();
        }

    }

    public interface DirectAbstractAdditionContract
    {
        void existing();

        void directAddition();
    }

    public interface NewParentContract
    {
        void inheritedAddition();
    }

    public interface InheritedAbstractAdditionContract extends NewParentContract
    {
        void existing();
    }

    public interface DefaultAndStaticAdditionContract
    {
        void existing();

        default void defaultAddition()
        {
        }

        static void staticAddition()
        {
        }
    }

    public interface IndependentNewContract
    {
        void independentMethod();
    }

    public interface ExactApprovedAdditionContract
    {
        void existing();

        void approved( String value );
    }

    public interface NonApprovedSignatureContract
    {
        void existing();

        void approved( int value );
    }

    public interface ApprovedOwnerContract
    {
        void existing();

        void approved( String value );
    }

    public interface CrossOwnerContract
    {
        void existing();

        void approved( String value );
    }

    public static class BaselineClassContract
    {
        public void approved( String value )
        {
        }
    }

    public interface ClassSignatureCollisionContract
    {
        void existing();

        void approved( String value );
    }

    public interface BaselineParentContract
    {
        void inheritedAddition();
    }

    public interface BaselineChildWithNewEdgeContract extends BaselineParentContract
    {
        void existing();
    }

}
