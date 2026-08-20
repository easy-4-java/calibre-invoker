# Calibre Invoker Complete Functionality Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make all ten checked-in Calibre command families executable through `DefaultInvoker.execute()` with correct cross-platform configuration, deterministic tests, compatible APIs, and enforced delivery gates.

**Architecture:** Keep the public `Invoker` facade and per-command builders, but extract request routing into `CommandLineBuilderRegistry`, executable discovery into `CalibreExecutableResolver`, and process launch into injectable `ProcessExecutor`. Every task follows red-green-refactor and leaves an independently testable commit.

**Tech Stack:** Java 17, Maven, JUnit 4.13.2, Plexus Utils `Commandline`, SLF4J 2.0.x, Lombok `@Slf4j`, JaCoCo, GitHub Actions.

**Spec:** `docs/superpowers/specs/2026-08-19-calibre-invoker-complete-functionality-design.md`

## Global Constraints

- Preserve existing public type and method descriptors; additions and deprecations are allowed, removals are not.
- Use `Objects.isNull/nonNull` for object null checks and imported `StringUtils` for strings.
- Use SLF4J for production logging; retain `InvokerLogger` as a compatibility API.
- Pass command arguments as individual tokens; never build a shell command string.
- Unit tests are offline and do not require Calibre.
- Target Java 17 on Linux, Windows, and macOS.
- Production code has no wildcard imports.
- Do not create or use a Git worktree.
- Before each task, run `git status --short` and preserve unrelated changes.

---

## File Map

### New production files

- `src/main/java/io/github/easy4j/calibre/invoker/ProcessExecutor.java` — process execution seam.
- `src/main/java/io/github/easy4j/calibre/invoker/PlexusProcessExecutor.java` — Plexus-backed production executor.
- `src/main/java/io/github/easy4j/calibre/invoker/command/CalibreExecutableResolver.java` — executable precedence and platform lookup.
- `src/main/java/io/github/easy4j/calibre/invoker/command/CommandLineBuilderRegistry.java` — request-to-builder factory mapping.
- `src/main/java/io/github/easy4j/calibre/invoker/command/EbookConvertCommandLineBuilder.java` — `ebook-convert` arguments.
- `src/main/java/io/github/easy4j/calibre/invoker/command/EbookEditCommandLineBuilder.java` — `ebook-edit` arguments.
- `src/main/java/io/github/easy4j/calibre/invoker/command/EbookMetaCommandLineBuilder.java` — `ebook-meta` arguments.
- `src/main/java/io/github/easy4j/calibre/invoker/command/EbookPolishCommandLineBuilder.java` — `ebook-polish` arguments.
- `src/main/java/io/github/easy4j/calibre/invoker/command/EbookViewerCommandLineBuilder.java` — `ebook-viewer` arguments.
- Five matching request interfaces under `src/main/java/io/github/easy4j/calibre/invoker/request/`.
- `src/main/java/io/github/easy4j/calibre/invoker/Slf4jInvokerLogger.java` — legacy logger adapter.

### Modified production files

- `DefaultInvoker.java` — registry, effective configuration, executor injection, checked errors.
- `AbstractCommandLineBuilder.java` — resolver integration, validation, environment, working directory.
- Existing five builders — validation and argument corrections.
- Existing ten default request classes — correct types, fluent returns, compatible overloads.
- `pom.xml` — test/build/API/coverage gates.

### Test support

- `src/test/java/io/github/easy4j/calibre/invoker/support/RecordingProcessExecutor.java` — captures a command without starting it.
- Unit tests mirror every production collaborator.
- `src/it/` — optional real-Calibre Failsafe fixtures, added only after offline gates pass.

---

### Task 1: Freeze the Public API and Baseline

**Files:**
- Modify: `pom.xml:277-315,595-634`
- Create: `src/test/java/io/github/easy4j/calibre/invoker/PublicApiBaselineTest.java`
- Create: `src/test/resources/api/calibre-invoker-2.0-public-types.txt`

**Interfaces:**
- Consumes: current compiled public classes.
- Produces: a sorted public-type baseline and a Maven verification gate that later tasks must preserve.

- [ ] **Step 1: Write the failing API baseline test**

```java
@Test
public void publicTypesMatchBaseline() throws Exception {
    List<String> expected = Files.readAllLines(
            Paths.get("src/test/resources/api/calibre-invoker-2.0-public-types.txt"),
            StandardCharsets.UTF_8);
    List<String> actual = PublicApiScanner.scan("io.github.easy4j.calibre.invoker");
    assertTrue(actual.containsAll(expected));
}
```

Implement `PublicApiScanner` as a package-private nested test helper that sorts public class and public method descriptors. Seed the baseline from the current checkout before changing production code.

- [ ] **Step 2: Run the baseline test and observe the missing fixture/helper failure**

Run: `mvn -Dtest=PublicApiBaselineTest test`

Expected: FAIL because the baseline fixture or scanner is not yet complete.

- [ ] **Step 3: Add the scanner, baseline fixture, and API-diff Maven execution**

Use a stable descriptor format such as:

```text
CLASS io.github.easy4j.calibre.invoker.Invoker
METHOD io.github.easy4j.calibre.invoker.Invoker#execute(InvocationRequest):InvocationResult
```

Configure the chosen API check in `verify` to reject removals while allowing additions. The fixture contains the descriptors that must remain; the test intentionally uses `containsAll`, not whole-list equality. Do not bind signing or deployment goals to ordinary verification.

- [ ] **Step 4: Verify the baseline**

Run: `mvn -Dtest=PublicApiBaselineTest test`

Expected: PASS with the current API represented exactly once and sorted.

- [ ] **Step 5: Commit**

```bash
git add pom.xml src/test/java/io/github/easy4j/calibre/invoker/PublicApiBaselineTest.java src/test/resources/api/calibre-invoker-2.0-public-types.txt
git commit -m "test: freeze calibre invoker public api"
```

### Task 2: Introduce the Process Execution Seam

**Files:**
- Create: `src/main/java/io/github/easy4j/calibre/invoker/ProcessExecutor.java`
- Create: `src/main/java/io/github/easy4j/calibre/invoker/PlexusProcessExecutor.java`
- Create: `src/test/java/io/github/easy4j/calibre/invoker/support/RecordingProcessExecutor.java`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/DefaultInvoker.java:58-150`
- Modify: `src/test/java/io/github/easy4j/calibre/invoker/DefaultInvokerTest.java`

**Interfaces:**
- Consumes: Plexus `Commandline`, `CommandLineException`, and existing output handlers.
- Produces: `int ProcessExecutor.execute(Commandline, InvocationOutputHandler, InvocationOutputHandler)` and package-visible `DefaultInvoker(ProcessExecutor)` for tests. Task 4 extends test construction with a registry.

- [ ] **Step 1: Write a failing executor delegation test**

```java
@Test
public void executeDelegatesToInjectedProcessExecutor() throws Exception {
    RecordingProcessExecutor executor = new RecordingProcessExecutor(7);
    DefaultInvoker invoker = new DefaultInvoker(executor);
    InvocationResult result = invoker.execute(validWeb2diskRequest());
    assertEquals(7, result.getExitCode());
    assertEquals("web2disk", executor.getExecutableName());
}
```

- [ ] **Step 2: Run the test and verify it fails at compilation**

Run: `mvn -Dtest=DefaultInvokerTest#executeDelegatesToInjectedProcessExecutor test`

Expected: FAIL because `ProcessExecutor` and the injected-executor constructor do not exist.

- [ ] **Step 3: Add the minimal interfaces and production adapter**

```java
public interface ProcessExecutor {
    int execute(Commandline commandline,
            InvocationOutputHandler outputHandler,
            InvocationOutputHandler errorHandler) throws CommandLineException;
}

public final class PlexusProcessExecutor implements ProcessExecutor {
    @Override
    public int execute(Commandline commandline,
            InvocationOutputHandler outputHandler,
            InvocationOutputHandler errorHandler) throws CommandLineException {
        return CommandLineUtils.executeCommandLine(commandline, outputHandler, errorHandler);
    }
}
```

The no-argument `DefaultInvoker()` constructs `PlexusProcessExecutor`; the package-visible single-argument test constructor stores the injected executor and continues to use the existing web2disk builder selection until Task 4 replaces routing.

- [ ] **Step 4: Run focused and existing invoker tests**

Run: `mvn -Dtest=DefaultInvokerTest,DefaultInvocationResultTest test`

Expected: PASS; no real process starts in the new test.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/io/github/easy4j/calibre/invoker src/test/java/io/github/easy4j/calibre/invoker
git commit -m "refactor: isolate calibre process execution"
```

### Task 3: Implement Cross-Platform Executable Resolution

**Files:**
- Create: `src/main/java/io/github/easy4j/calibre/invoker/command/CalibreExecutableResolver.java`
- Create: `src/test/java/io/github/easy4j/calibre/invoker/command/CalibreExecutableResolverTest.java`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/command/AbstractCommandLineBuilder.java:46-305`

**Interfaces:**
- Consumes: command name, request home, invoker home, system property, environment, OS family, PATH.
- Produces: `File resolve(String commandName, File requestHome, File invokerHome) throws CommandLineConfigurationException`.

- [ ] **Step 1: Write precedence and PATH tests**

```java
@Test
public void requestHomeWinsOverEveryOtherSource() throws Exception {
    resolver = resolverWith(pathMap("/request/web2disk", true), "linux");
    File result = resolver.resolve("web2disk", file("/request"), file("/invoker"));
    assertEquals(file("/request/web2disk"), result);
}

@Test
public void pathIsUsedWhenNoHomeIsConfigured() throws Exception {
    resolver = resolverWith(pathMap("/usr/bin/web2disk", true), "linux");
    assertEquals(file("/usr/bin/web2disk"), resolver.resolve("web2disk", null, null));
}
```

Add Windows `.exe`, macOS bundle, invalid directory, and not-found cases in the same test class.

- [ ] **Step 2: Run resolver tests**

Run: `mvn -Dtest=CalibreExecutableResolverTest test`

Expected: FAIL because the resolver is absent.

- [ ] **Step 3: Implement deterministic resolution**

```java
public File resolve(String commandName, File requestHome, File invokerHome)
        throws CommandLineConfigurationException {
    String executable = windows ? commandName + ".exe" : commandName;
    for (File home : orderedHomes(requestHome, invokerHome)) {
        File candidate = new File(home, executable);
        if (fileProbe.isExecutable(candidate)) {
            return fileProbe.canonical(candidate);
        }
    }
    return resolveFromPathOrMacBundle(executable);
}
```

Inject property/environment/file probes through package-visible constructor interfaces. Error text lists source names only.

- [ ] **Step 4: Run resolver and builder tests**

Run: `mvn -Dtest=CalibreExecutableResolverTest,AbstractCommandLineBuilderTest test`

Expected: PASS; existing builders can inject a fixed resolver in tests.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/io/github/easy4j/calibre/invoker/command src/test/java/io/github/easy4j/calibre/invoker/command
git commit -m "feat: resolve calibre executables across platforms"
```

### Task 4: Add the Builder Registry and Checked Routing Errors

**Files:**
- Create: `src/main/java/io/github/easy4j/calibre/invoker/command/CommandLineBuilderRegistry.java`
- Create: `src/test/java/io/github/easy4j/calibre/invoker/command/CommandLineBuilderRegistryTest.java`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/DefaultInvoker.java:82-124`
- Modify: `src/test/java/io/github/easy4j/calibre/invoker/DefaultInvokerTest.java`

**Interfaces:**
- Consumes: `Class<? extends InvocationRequest>` and `Supplier<? extends AbstractCommandLineBuilder>`.
- Produces: `AbstractCommandLineBuilder create(InvocationRequest request) throws CommandLineConfigurationException`.

- [ ] **Step 1: Write null, unsupported, duplicate, and known-route tests**

```java
@Test(expected = CommandLineConfigurationException.class)
public void duplicateRegistrationIsRejected() throws Exception {
    new CommandLineBuilderRegistry()
            .register(Web2diskInvocationRequest.class, Web2diskCommandLineBuilder::new)
            .register(Web2diskInvocationRequest.class, Web2diskCommandLineBuilder::new);
}

@Test
public void unsupportedRequestBecomesCheckedInvocationError() {
    assertCalibreInvocationException(() -> new DefaultInvoker().execute(new UnknownRequest()));
}
```

- [ ] **Step 2: Run focused tests**

Run: `mvn -Dtest=CommandLineBuilderRegistryTest,DefaultInvokerTest test`

Expected: FAIL because unknown requests still yield null/NPE.

- [ ] **Step 3: Implement exact-interface registration and assignable lookup**

```java
public AbstractCommandLineBuilder create(InvocationRequest request)
        throws CommandLineConfigurationException {
    if (Objects.isNull(request)) {
        throw new CommandLineConfigurationException("Invocation request must not be null.");
    }
    return factories.entrySet().stream()
            .filter(entry -> entry.getKey().isInstance(request))
            .findFirst()
            .map(entry -> entry.getValue().get())
            .orElseThrow(() -> new CommandLineConfigurationException(
                    "Unsupported invocation request: " + request.getClass().getName()));
}
```

Initially register only the five builders that exist. Later tasks expand the default map to ten.

- [ ] **Step 4: Run routing tests and ensure no NPE remains**

Run: `mvn -Dtest=CommandLineBuilderRegistryTest,DefaultInvokerTest test`

Expected: PASS with checked errors containing the request class.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/io/github/easy4j/calibre/invoker/DefaultInvoker.java src/main/java/io/github/easy4j/calibre/invoker/command/CommandLineBuilderRegistry.java src/test/java/io/github/easy4j/calibre/invoker
git commit -m "feat: route calibre requests through builder registry"
```

### Task 5: Correct Common Command Lifecycle and Precedence

**Files:**
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/DefaultInvoker.java:95-223`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/command/AbstractCommandLineBuilder.java:73-302`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/request/AbstractInvocationRequest.java:38-207`
- Modify: `src/test/java/io/github/easy4j/calibre/invoker/DefaultInvokerTest.java`
- Modify: `src/test/java/io/github/easy4j/calibre/invoker/command/AbstractCommandLineBuilderTest.java`

**Interfaces:**
- Consumes: request/invoker Calibre Home, request/invoker handlers, invoker working directory.
- Produces: an effective configuration applied to one `Commandline`.

- [ ] **Step 1: Write failing effective-configuration tests**

```java
@Test
public void requestCalibreHomeOverridesInvokerHome() throws Exception {
    request.setCalibreHome(requestHome);
    invoker.setCalibreHome(invokerHome);
    invoker.execute(request);
    assertEquals(new File(requestHome, executableName), recorder.getExecutable());
}

@Test
public void workingDirectoryIsAppliedToCommandline() throws Exception {
    invoker.setWorkingDirectory(temp.getRoot());
    invoker.execute(request);
    assertEquals(temp.getRoot(), recorder.getWorkingDirectory());
}
```

Also assert request handler > invoker handler > default handler and environment inheritance disabled.

- [ ] **Step 2: Run lifecycle tests**

Run: `mvn -Dtest=DefaultInvokerTest,AbstractCommandLineBuilderTest test`

Expected: FAIL for request-home executable selection and working directory.

- [ ] **Step 3: Apply effective values once**

```java
File effectiveHome = Objects.nonNull(request.getCalibreHome())
        ? request.getCalibreHome() : getCalibreHome();
cliBuilder.setCalibreHome(effectiveHome);
cliBuilder.setWorkingDirectory(getWorkingDirectory());
```

In `build`:

```java
if (Objects.nonNull(workingDirectory)) {
    cli.setWorkingDirectory(workingDirectory);
}
```

Validate that configured working directories exist and are directories. Keep non-zero exit codes in `InvocationResult`.

- [ ] **Step 4: Run all common lifecycle tests**

Run: `mvn -Dtest=DefaultInvokerTest,AbstractCommandLineBuilderTest,DefaultInvocationResultTest test`

Expected: PASS for precedence, handlers, environment, working directory, exit code, and start exception.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/io/github/easy4j/calibre/invoker src/test/java/io/github/easy4j/calibre/invoker
git commit -m "fix: apply effective calibre invocation configuration"
```

### Task 6: Repair Web2disk and Fetch Metadata

**Files:**
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/request/DefaultWeb2diskInvocationRequest.java:24-244`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/request/FetchEbookMetadataInvocationRequest.java`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/request/DefaultFetchEbookMetadataInvocationRequest.java:27-183`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/command/Web2diskCommandLineBuilder.java:39-177`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/command/FetchEbookMetadataCommandLineBuilder.java:40-153`
- Modify: matching request and builder tests.

**Interfaces:**
- Consumes: existing web2disk API and corrected metadata values.
- Produces: valid token arrays for `web2disk` and `fetch-ebook-metadata`.

- [ ] **Step 1: Write exact token and validation tests**

```java
@Test
public void setDelayRemainsFluent() {
    DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
    assertSame(request, request.setDelay(2));
}

@Test
public void fetchMetadataUsesStringValues() throws Exception {
    request.setTitle("Domain-Driven Design");
    request.setAuthors("Eric Evans");
    request.setIsbn("9780321125217");
    assertArrayEquals(new String[] {
            "--authors", "Eric Evans", "--isbn", "9780321125217",
            "--timeout", "30", "--title", "Domain-Driven Design"
    }, arguments(builder.build(request)));
}
```

Add web2disk URL-required, negative delay, max recursion, timeout, and path-with-spaces cases.

- [ ] **Step 2: Run the two builder/request suites**

Run: `mvn -Dtest=DefaultWeb2diskInvocationRequestTest,Web2diskCommandLineBuilderTest,DefaultFetchEbookMetadataInvocationRequestTest,FetchEbookMetadataCommandLineBuilderTest test`

Expected: FAIL for `setDelay`, boolean metadata values, missing helper calls, and current cover NPE.

- [ ] **Step 3: Implement typed metadata and corrected builder sequence**

```java
public InvocationRequest setDelay(int delay) {
    this.delay = delay;
    return this;
}

private String title;
private String authors;
private String isbn;
private final List<String> allowedPlugins = new ArrayList<>();
```

Emit `setAllowedPlugins`, `setAuthors`, `setCoverFile`, `setIsbn`, `setOpf`, `setTimeout`, and `setTitle` in that order. Retain deprecated boolean setters as adapters without using them in the builder.

- [ ] **Step 4: Run focused tests and facade integration**

Run: `mvn -Dtest=DefaultWeb2diskInvocationRequestTest,Web2diskCommandLineBuilderTest,DefaultFetchEbookMetadataInvocationRequestTest,FetchEbookMetadataCommandLineBuilderTest,DefaultInvokerTest test`

Expected: PASS and both request types reach the recording executor.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/io/github/easy4j/calibre/invoker/request src/main/java/io/github/easy4j/calibre/invoker/command src/test/java/io/github/easy4j/calibre/invoker
git commit -m "fix: complete web and metadata command support"
```

### Task 7: Repair and Connect the Three LRF Commands

**Files:**
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/command/Lrf2lrsCommandLineBuilder.java:39-121`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/command/Lrs2lrfCommandLineBuilder.java:38-120`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/command/LrfviewerCommandLineBuilder.java:38-120`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/command/CommandLineBuilderRegistry.java`
- Modify: matching builder and `DefaultInvokerTest` files.

**Interfaces:**
- Consumes: existing `Lrf2lrsInvocationRequest`, `Lrs2lrfInvocationRequest`, `LrfviewerInvocationRequest`.
- Produces: registered `lrf2lrs`, `lrs2lrf`, and `lrfviewer` command lines.

- [ ] **Step 1: Write regression tests for the known type/executable defects**

```java
@Test
public void lrf2lrsAcceptsItsOwnRequestInterface() throws Exception {
    assertEquals("book.lrf", lastArgument(builder.build(validLrf2lrsRequest())));
}

@Test
public void lrfViewerUsesLrfViewerExecutable() throws Exception {
    assertEquals(platformExecutable("lrfviewer"),
            executableName(builder.build(validLrfviewerRequest())));
}
```

Add one facade route test per LRF request.

- [ ] **Step 2: Run all LRF tests**

Run: `mvn -Dtest=Lrf2lrsCommandLineBuilderTest,Lrs2lrfCommandLineBuilderTest,LrfviewerCommandLineBuilderTest,DefaultInvokerTest test`

Expected: FAIL at the wrong interface check, wrong executable, and missing registry routes.

- [ ] **Step 3: Correct request checks, casts, executables, and registration**

```java
if (!(request instanceof Lrf2lrsInvocationRequest)) {
    throw unsupportedRequest(request, Lrf2lrsInvocationRequest.class);
}
Lrf2lrsInvocationRequest typed = (Lrf2lrsInvocationRequest) request;
```

Set executable names to `lrf2lrs`, `lrs2lrf`, and `lrfviewer`; validate each required input file before dereferencing it.

- [ ] **Step 4: Run request, builder, and facade tests**

Run: `mvn -Dtest='*Lrf*Test,*Lrs*Test,DefaultInvokerTest' test`

Expected: PASS for all three direct and facade paths.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/io/github/easy4j/calibre/invoker/command src/test/java/io/github/easy4j/calibre/invoker
git commit -m "fix: connect legacy lrf calibre commands"
```

### Task 8: Define Correct Typed Contracts for the Five Missing Commands

**Files:**
- Create: `src/main/java/io/github/easy4j/calibre/invoker/request/EbookConvertInvocationRequest.java`
- Create: `src/main/java/io/github/easy4j/calibre/invoker/request/EbookEditInvocationRequest.java`
- Create: `src/main/java/io/github/easy4j/calibre/invoker/request/EbookMetaInvocationRequest.java`
- Create: `src/main/java/io/github/easy4j/calibre/invoker/request/EbookPolishInvocationRequest.java`
- Create: `src/main/java/io/github/easy4j/calibre/invoker/request/EbookViewerInvocationRequest.java`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/request/DefaultEbookConvertInvocationRequest.java`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/request/DefaultEbookEditInvocationRequest.java`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/request/DefaultEbookMetaInvocationRequest.java`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/request/DefaultEbookPolishInvocationRequest.java`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/request/DefaultEbookViewerInvocationRequest.java`
- Modify: the five matching request tests under `src/test/java/io/github/easy4j/calibre/invoker/request/`.

**Interfaces:**
- Consumes: `InvocationRequest` common configuration.
- Produces: the following exact stable methods:

```java
interface EbookConvertInvocationRequest extends InvocationRequest {
    File getInputFile(); File getOutputFile(); List<String> getAdditionalArguments();
    EbookConvertInvocationRequest setInputFile(File value);
    EbookConvertInvocationRequest setOutputFile(File value);
    EbookConvertInvocationRequest addArgument(String value);
}

interface EbookEditInvocationRequest extends InvocationRequest {
    File getEbookFile(); List<String> getAdditionalArguments();
    EbookEditInvocationRequest setEbookFile(File value);
    EbookEditInvocationRequest addArgument(String value);
}

interface EbookMetaInvocationRequest extends InvocationRequest {
    File getEbookFile(); String getAuthorSort(); File getFromOpf();
    File getCoverOutput(); String getTitleSort(); List<String> getIdentifiers();
}

interface EbookPolishInvocationRequest extends InvocationRequest {
    File getInputFile(); File getOutputFile(); boolean isCompressImages();
    boolean isJacket(); boolean isSmartenPunctuation(); boolean isSubsetFonts();
    boolean isUpgradeBook();
}

interface EbookViewerInvocationRequest extends InvocationRequest {
    File getEbookFile(); boolean isContinueReading(); boolean isFullscreen();
    boolean isNewInstance(); boolean isRaiseWindow(); boolean isDetach();
    String getOpenAt();
}
```

- [ ] **Step 1: Rewrite request tests around typed values and fluent identity**

```java
@Test
public void convertRequestCarriesFilesAndOrderedArguments() {
    DefaultEbookConvertInvocationRequest request = new DefaultEbookConvertInvocationRequest();
    assertSame(request, request.setInputFile(input));
    assertSame(request, request.setOutputFile(output));
    assertSame(request, request.addArgument("--pretty-print"));
    assertEquals(Arrays.asList("--pretty-print"), request.getAdditionalArguments());
}
```

Repeat explicit assertions for every method listed in the produced interfaces.

- [ ] **Step 2: Run the five request suites**

Run: `mvn -Dtest=DefaultEbookConvertInvocationRequestTest,DefaultEbookEditInvocationRequestTest,DefaultEbookMetaInvocationRequestTest,DefaultEbookPolishInvocationRequestTest,DefaultEbookViewerInvocationRequestTest test`

Expected: FAIL because interfaces and correctly typed methods are absent.

- [ ] **Step 3: Add interfaces and adapt default implementations**

Use defensive unmodifiable views for argument/identifier lists. Keep old boolean/void setters as deprecated overloads where they currently exist; new fluent methods return the concrete typed interface. For legacy boolean metadata setters, `false` clears the corresponding value and `true` without a string/file value makes builder validation fail with a field-specific checked configuration error instead of inventing a value.

```java
@Override
public List<String> getAdditionalArguments() {
    return Collections.unmodifiableList(additionalArguments);
}
```

- [ ] **Step 4: Run request suites and public API check**

Run: `mvn -Dtest='DefaultEbook*InvocationRequestTest,PublicApiBaselineTest' test`

Expected: PASS; API report contains additions/deprecations but no removals.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/io/github/easy4j/calibre/invoker/request src/test/java/io/github/easy4j/calibre/invoker/request src/test/resources/api
git commit -m "feat: define typed ebook invocation contracts"
```

### Task 9: Implement Ebook Convert and Ebook Meta Builders

**Files:**
- Create: `src/main/java/io/github/easy4j/calibre/invoker/command/EbookConvertCommandLineBuilder.java`
- Create: `src/main/java/io/github/easy4j/calibre/invoker/command/EbookMetaCommandLineBuilder.java`
- Create: matching test classes.
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/command/CommandLineBuilderRegistry.java`
- Modify: `src/test/java/io/github/easy4j/calibre/invoker/DefaultInvokerTest.java`

**Interfaces:**
- Consumes: `EbookConvertInvocationRequest`, `EbookMetaInvocationRequest`.
- Produces: `ebook-convert input output [arguments]` and `ebook-meta ebook [options]`.

- [ ] **Step 1: Write exact argument and validation tests**

```java
@Test
public void convertPlacesInputAndOutputBeforeOptions() throws Exception {
    request.setInputFile(input).setOutputFile(output).addArgument("--pretty-print");
    assertArrayEquals(new String[] {
            input.getCanonicalPath(), output.getCanonicalPath(), "--pretty-print"
    }, arguments(builder.build(request)));
}

@Test
public void metaEmitsTypedValueOptions() throws Exception {
    request.setEbookFile(book).setAuthorSort("Evans, Eric").setTitleSort("DDD");
    assertArrayEquals(new String[] {
            book.getCanonicalPath(), "--author-sort", "Evans, Eric", "--title-sort", "DDD"
    }, arguments(builder.build(request)));
}
```

Add missing input/output and null additional-argument rejection.

- [ ] **Step 2: Run new builder tests**

Run: `mvn -Dtest=EbookConvertCommandLineBuilderTest,EbookMetaCommandLineBuilderTest test`

Expected: FAIL because builders do not exist.

- [ ] **Step 3: Implement both builders and register them**

Each builder overrides `commandName()` with `ebook-convert` or `ebook-meta`, validates its typed request, canonicalizes file values, and emits ordered tokens. `addArgument` rejects null/blank values but preserves caller order.

```java
for (String argument : request.getAdditionalArguments()) {
    cli.createArg().setValue(argument);
}
```

- [ ] **Step 4: Run builders, facade routes, and API check**

Run: `mvn -Dtest=EbookConvertCommandLineBuilderTest,EbookMetaCommandLineBuilderTest,DefaultInvokerTest,PublicApiBaselineTest test`

Expected: PASS with both command names captured by `RecordingProcessExecutor`.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/io/github/easy4j/calibre/invoker/command src/test/java/io/github/easy4j/calibre/invoker
git commit -m "feat: add ebook convert and metadata commands"
```

### Task 10: Implement Ebook Edit, Polish, and Viewer Builders

**Files:**
- Create: `src/main/java/io/github/easy4j/calibre/invoker/command/EbookEditCommandLineBuilder.java`
- Create: `src/main/java/io/github/easy4j/calibre/invoker/command/EbookPolishCommandLineBuilder.java`
- Create: `src/main/java/io/github/easy4j/calibre/invoker/command/EbookViewerCommandLineBuilder.java`
- Create: `src/test/java/io/github/easy4j/calibre/invoker/command/EbookEditCommandLineBuilderTest.java`
- Create: `src/test/java/io/github/easy4j/calibre/invoker/command/EbookPolishCommandLineBuilderTest.java`
- Create: `src/test/java/io/github/easy4j/calibre/invoker/command/EbookViewerCommandLineBuilderTest.java`
- Modify: `src/main/java/io/github/easy4j/calibre/invoker/command/CommandLineBuilderRegistry.java`
- Modify: `src/test/java/io/github/easy4j/calibre/invoker/DefaultInvokerTest.java`

**Interfaces:**
- Consumes: typed edit, polish, and viewer requests from Task 8.
- Produces: registered `ebook-edit`, `ebook-polish`, and `ebook-viewer` command lines.

- [ ] **Step 1: Write exact token tests**

```java
@Test
public void polishEmitsInputOutputAndSelectedOperations() throws Exception {
    request.setInputFile(input).setOutputFile(output)
            .setSmartenPunctuation(true).setSubsetFonts(true);
    assertArrayEquals(new String[] {
            input.getCanonicalPath(), output.getCanonicalPath(),
            "--smarten-punctuation", "--subset-fonts"
    }, arguments(builder.build(request)));
}

@Test
public void viewerAllowsContinueWithoutFile() throws Exception {
    request.setContinueReading(true).setFullscreen(true);
    assertArrayEquals(new String[] {"--continue", "--full-screen"},
            arguments(builder.build(request)));
}
```

For edit, assert file first then ordered additional arguments. For viewer, require a file unless continue-reading is true.

- [ ] **Step 2: Run new builder tests**

Run: `mvn -Dtest=EbookEditCommandLineBuilderTest,EbookPolishCommandLineBuilderTest,EbookViewerCommandLineBuilderTest test`

Expected: FAIL because builders do not exist.

- [ ] **Step 3: Implement and register all three builders**

Use executable names `ebook-edit`, `ebook-polish`, and `ebook-viewer`. Emit polish flags in the stable order `--compress-images`, `--jacket`, `--smarten-punctuation`, `--subset-fonts`, `--upgrade-book`. Emit viewer flags in the order `--continue`, `--detach`, `--full-screen`, `--new-instance`, `--open-at value`, `--raise-window`, then optional file.

- [ ] **Step 4: Run all ten facade-route tests**

Run: `mvn -Dtest='*CommandLineBuilderTest,DefaultInvokerTest' test`

Expected: PASS; a registry completeness assertion reports exactly ten default request-interface mappings.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/io/github/easy4j/calibre/invoker/command src/test/java/io/github/easy4j/calibre/invoker
git commit -m "feat: add ebook edit polish and viewer commands"
```

### Task 11: Move Production Logging to SLF4J and Bound Captured Output

**Files:**
- Create: `src/main/java/io/github/easy4j/calibre/invoker/Slf4jInvokerLogger.java`
- Create: `src/main/java/io/github/easy4j/calibre/invoker/BoundedInvocationOutputHandler.java`
- Create: matching tests.
- Modify: `DefaultInvoker.java`, `AbstractCommandLineBuilder.java`, and `pom.xml` logging dependencies.

**Interfaces:**
- Consumes: legacy `InvokerLogger`, `InvocationOutputHandler`.
- Produces: SLF4J-backed compatibility logger and bounded UTF-8 output capture.

- [ ] **Step 1: Write redaction and byte-limit tests**

```java
@Test
public void commandLogRedactsUrlUserInfo() {
    assertEquals("https://***@example.com/book", redactor.redact(
            "https://alice:secret@example.com/book"));
}

@Test
public void captureNeverExceedsConfiguredBytes() {
    BoundedInvocationOutputHandler handler = new BoundedInvocationOutputHandler(8);
    handler.consumeLine("1234567890");
    assertTrue(handler.getOutput().getBytes(StandardCharsets.UTF_8).length <= 8);
    assertTrue(handler.isTruncated());
}
```

- [ ] **Step 2: Run logging/output tests**

Run: `mvn -Dtest=Slf4jInvokerLoggerTest,BoundedInvocationOutputHandlerTest test`

Expected: FAIL because classes are absent.

- [ ] **Step 3: Implement the adapter, redactor, and bounded handler**

Use Lombok `@Slf4j` only in concrete classes. Do not log custom environment values. Keep `SystemOutLogger` and `PrintStreamLogger` public and functional, but make the default logger `Slf4jInvokerLogger`.

```java
private static final Pattern URL_USER_INFO =
        Pattern.compile("(https?://)[^/@\\s]+@");
```

- [ ] **Step 4: Run logging and full unit regression**

Run: `mvn test`

Expected: PASS; Surefire reports no network-dependent failure.

- [ ] **Step 5: Commit**

```bash
git add pom.xml src/main/java/io/github/easy4j/calibre/invoker src/test/java/io/github/easy4j/calibre/invoker
git commit -m "refactor: use slf4j and bound invocation output"
```

### Task 12: Add Cross-Platform Integration and Delivery Gates

**Files:**
- Modify: `.github/workflows/ci.yml`
- Modify: `pom.xml`
- Create: `src/test/java/io/github/easy4j/calibre/invoker/CommandEnvironmentIntegrationTest.java`
- Create: `.github/workflows/calibre-e2e.yml`
- Create: `src/it/java/io/github/easy4j/calibre/invoker/CalibreCliIT.java`
- Add: minimal legal test fixtures under `src/it/resources/fixtures/`.

**Interfaces:**
- Consumes: completed facade and Maven build.
- Produces: offline three-OS PR gate and opt-in real-Calibre Failsafe workflow.

- [ ] **Step 1: Add a temporary-executable integration test**

```java
@Test
public void childProcessReceivesWorkingDirectoryAndEnvironment() throws Exception {
    InvocationResult result = fixtureInvoker()
            .setWorkingDirectory(temp.getRoot())
            .execute(requestWithEnvironment("CALIBRE_INVOKER_TEST", "ok"));
    assertEquals(0, result.getExitCode());
    assertEquals("ok", readFixtureEnvironment());
    assertEquals(temp.getRoot().getCanonicalPath(), readFixtureWorkingDirectory());
}
```

Generate platform-specific temporary scripts inside the test and mark Unix scripts executable.

- [ ] **Step 2: Run offline verify before changing CI**

Run: `mvn -B --no-transfer-progress clean verify`

Expected: PASS locally; coverage report exists at `target/site/jacoco/index.html`.

- [ ] **Step 3: Enforce coverage and configure workflows**

Set JaCoCo `haltOnFailure=true`; enforce 90% line and branch coverage for invoker/command packages. Change PR CI to a matrix of `ubuntu-latest`, `windows-latest`, and `macos-latest`, all on Temurin 17. Configure Failsafe real-Calibre tests only in `calibre-e2e.yml` with manual and scheduled triggers, and record `calibre --version` before tests.

- [ ] **Step 4: Verify Maven profiles and workflow syntax**

Run: `mvn -B --no-transfer-progress clean verify`

Run: `mvn -B --no-transfer-progress -Pcalibre-e2e -DskipCalibreE2E=true verify`

Expected: both PASS; the second command proves profile wiring without requiring Calibre.

- [ ] **Step 5: Commit**

```bash
git add pom.xml .github/workflows src/test src/it
git commit -m "test: enforce cross-platform calibre delivery gates"
```

### Task 13: Align Documentation and Perform Release Verification

**Files:**
- Modify: `README.md`
- Modify: `README.zh-CN.md`
- Modify: Javadocs in all public request/builder/facade classes.
- Create: `CHANGELOG.md` if absent; otherwise update it.

**Interfaces:**
- Consumes: verified implementation and CI evidence.
- Produces: accurate support matrix, examples, compatibility notes, and release checklist.

- [ ] **Step 1: Add documentation assertions**

Create a test in `PublicApiBaselineTest` that reads both READMEs and asserts every supported command name is present, `main` is documented as Java 17, and stale claims such as “No CI workflow files are present” are absent.

```java
assertFalse(readme.contains("No CI workflow files are present"));
assertTrue(readme.contains("mvn -B --no-transfer-progress clean verify"));
```

- [ ] **Step 2: Run the documentation assertion**

Run: `mvn -Dtest=PublicApiBaselineTest test`

Expected: FAIL against the current stale README statements.

- [ ] **Step 3: Update documentation from verified behavior**

Document all ten commands, configuration precedence, PATH/macOS resolution, working directory, handler precedence, result/error semantics, thread-safety statement, offline tests, real-Calibre profile, and one compiling example per command family. Mark network and GUI commands clearly.

- [ ] **Step 4: Run final verification and inspect evidence**

Run: `mvn -B --no-transfer-progress clean verify`

Run: `git diff --check`

Run: `codegraph sync && codegraph status`

Expected: Maven SUCCESS with non-zero test count, JaCoCo gate PASS, API compatibility PASS, no whitespace errors, and CodeGraph index up to date. Do not publish or deploy in this task.

- [ ] **Step 5: Commit**

```bash
git add README.md README.zh-CN.md CHANGELOG.md src/main/java src/test/java
git commit -m "docs: publish complete calibre command support matrix"
```

---

## Final Completion Gate

- [ ] All thirteen task commits exist and each task's focused tests passed before its commit.
- [ ] `mvn -B --no-transfer-progress clean verify` reports BUILD SUCCESS and actual executed test counts.
- [ ] Registry completeness test proves exactly ten supported request interfaces.
- [ ] Null/unknown requests produce `CalibreInvocationException`, never NPE/ClassCastException.
- [ ] Linux, Windows, and macOS CI all pass on Java 17.
- [ ] JaCoCo line and branch gates are enforced at 90% for core packages.
- [ ] Public API check reports no removals.
- [ ] Real-Calibre E2E remains separately gated and records the tested Calibre version.
- [ ] README/Javadoc claims match executable tests.
- [ ] `git diff --check` passes and unrelated user changes remain untouched.
