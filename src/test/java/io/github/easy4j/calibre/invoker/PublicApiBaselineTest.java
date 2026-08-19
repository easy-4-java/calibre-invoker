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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.junit.Test;

public class PublicApiBaselineTest
{

    private static final Set<String> APPROVED_ADDITIVE_ABSTRACT_METHODS = new HashSet<>( Arrays.asList(
            "METHOD io.github.easy4j.calibre.invoker.request.FetchEbookMetadataInvocationRequest#addAllowedPlugin(String):InvocationRequest",
            "METHOD io.github.easy4j.calibre.invoker.request.FetchEbookMetadataInvocationRequest#setAllowedPlugins(List):InvocationRequest",
            "METHOD io.github.easy4j.calibre.invoker.request.FetchEbookMetadataInvocationRequest#setAuthors(String):InvocationRequest",
            "METHOD io.github.easy4j.calibre.invoker.request.FetchEbookMetadataInvocationRequest#setIsbn(String):InvocationRequest",
            "METHOD io.github.easy4j.calibre.invoker.request.FetchEbookMetadataInvocationRequest#setTitle(String):InvocationRequest" ) );

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
        Set<String> approvedMethods = new HashSet<>( baseline );
        approvedMethods.addAll( APPROVED_ADDITIVE_ABSTRACT_METHODS );

        assertEquals( Collections.emptyList(), PublicApiScanner.findUnapprovedAbstractInterfaceMethods(
                "io.github.easy4j.calibre.invoker", new HashSet<>( baseline ), approvedMethods ) );
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
                Set<String> baselineDescriptors, Set<String> approvedMethods ) throws Exception
        {
            Path classesDirectory = Paths.get( Invoker.class.getProtectionDomain().getCodeSource().getLocation().toURI() );
            Path packageDirectory = classesDirectory.resolve( packageName.replace( '.', '/' ) );
            TreeSet<String> violations = new TreeSet<>();
            try ( Stream<Path> paths = Files.walk( packageDirectory ) )
            {
                paths.filter( Files::isRegularFile )
                        .filter( path -> path.toString().endsWith( ".class" ) )
                        .map( packageDirectory::relativize )
                        .map( path -> toClassName( packageName, path ) )
                        .forEach( className -> addUnapprovedAbstractMethods( violations, className,
                                baselineDescriptors, approvedMethods ) );
            }
            return new ArrayList<>( violations );
        }

        private static void addUnapprovedAbstractMethods( TreeSet<String> violations, String className,
                Set<String> baselineDescriptors, Set<String> approvedMethods )
        {
            try
            {
                Class<?> type = Class.forName( className, false, Invoker.class.getClassLoader() );
                if ( !Modifier.isPublic( type.getModifiers() ) || !type.isInterface()
                        || !baselineDescriptors.contains( "CLASS " + type.getName() ) )
                {
                    return;
                }
                for ( Method method : type.getDeclaredMethods() )
                {
                    if ( Modifier.isPublic( method.getModifiers() )
                            && Modifier.isAbstract( method.getModifiers() )
                            && !method.isBridge() && !method.isSynthetic() )
                    {
                        String descriptor = methodDescriptor( type, method );
                        if ( !approvedMethods.contains( descriptor ) )
                        {
                            violations.add( descriptor );
                        }
                    }
                }
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
            List<String> parameterTypes = new ArrayList<>();
            for ( Class<?> parameterType : method.getParameterTypes() )
            {
                parameterTypes.add( parameterType.getSimpleName() );
            }
            return "METHOD " + type.getName() + "#" + method.getName() + "("
                    + String.join( ",", parameterTypes ) + "):" + method.getReturnType().getSimpleName();
        }

    }

}
