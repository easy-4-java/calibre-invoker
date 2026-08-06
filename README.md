# calibre-invoker

[English](./README.md) | [简体中文](./README.zh-CN.md)

[![Java](https://img.shields.io/badge/Java-8-orange)] [![License](https://img.shields.io/badge/license-Apache%202.0-green)](./LICENSE)

> A Java wrapper that programmatically invokes the [Calibre](https://calibre-ebook.com)
> command line tool (ebook-convert, ebook-edit, ebook-polish, ebook-viewer, ebook-meta,
> fetch-ebook-metadata, web2disk, lrf2lrs, lrs2lrf, ...) from your application.

## Table of Contents

- [1. Project Overview](#1-project-overview)
- [2. Features & Status](#2-features--status)
- [3. Requirements & Compatibility](#3-requirements--compatibility)
- [4. Architecture & Modules](#4-architecture--modules)
- [5. Installation](#5-installation)
- [6. Quick Start](#6-quick-start)
- [7. Configuration](#7-configuration)
- [8. Core Usage / API](#8-core-usage--api)
- [9. Testing & Build](#9-testing--build)
- [10. Versioning & Branches](#10-versioning--branches)
- [11. Contributing & License](#11-contributing--license)

## 1. Project Overview

`calibre-invoker` is a small library that lets Java applications launch and control
[Calibre](https://calibre-ebook.com) command line tools as child processes. It follows
the classic "invoker" pattern: you build an `InvocationRequest` describing the options,
hand it to an `Invoker`, and receive an `InvocationResult` with the process exit code.

The library is a thin wrapper around Calibre executables — it does **not** implement
any ebook conversion logic itself, and it does **not** require a running Calibre
library service. **Calibre must be installed and working on your system.**

The command line reference is the official
[Calibre CLI index](https://manual.calibre-ebook.com/generated/en/cli-index.html).

Typical scenarios:

| Scenario | Invocation request |
| :--- | :--- |
| Convert an ebook from one format to another | `DefaultEbookConvertInvocationRequest` (`ebook-convert`) |
| Download a website as an ebook / web archive | `DefaultWeb2diskInvocationRequest` (`web2disk`) |
| Edit / polish ebook metadata | `DefaultEbookEditInvocationRequest` / `DefaultEbookPolishInvocationRequest` |
| Read ebook metadata | `DefaultEbookMetaInvocationRequest` / `DefaultFetchEbookMetadataInvocationRequest` (`ebook-meta`, `fetch-ebook-metadata`) |
| Convert LRF <-> LRS formats | `DefaultLrf2lrsInvocationRequest` / `DefaultLrs2lrfInvocationRequest` |
| Launch the ebook viewer | `DefaultEbookViewerInvocationRequest` (`ebook-viewer`) |

## 2. Features & Status

| Capability | Status | Notes |
| :--- | :--- | :--- |
| `Invoker` / `DefaultInvoker` facade | Stable | `execute(InvocationRequest)`, working directory, Calibre home, logger and output/error handlers |
| Typed invocation requests | Stable | One `Default*InvocationRequest` per Calibre command (convert, edit, meta, polish, viewer, web2disk, fetch-ebook-metadata, lrf2lrs, lrs2lrf) |
| Command line builders | Stable | `AbstractCommandLineBuilder` + per-command builders assemble the OS command line |
| Output capture | Stable | Pluggable `InvocationOutputHandler` (`SystemOutHandler`, `PrintStreamHandler`) |
| Logging | Stable | Pluggable `InvokerLogger` (`SystemOutLogger`, `PrintStreamLogger`) |
| Exit-code result model | Stable | `InvocationResult.getExitCode()`, `getExecutionException()` |

## 3. Requirements & Compatibility

| Requirement | Version / Notes |
| :--- | :--- |
| JDK | 8+ |
| Maven | 3.0+ (enforced; Maven Wrapper `./mvnw` included) |
| Calibre | Must be installed and on `PATH`, or located via `calibre.home` system property / `CALIBRE_HOME` environment variable |

Version lines:

| Branch | JDK | Version |
| :--- | :--- | :--- |
| `feature/1.0.x` | 8 | `1.0.x.*` |
| `feature/2.0.x` | 17 | `2.0.x.*` |
| `feature/3.0.x` | 21 | `3.0.x.*` |

## 4. Architecture & Modules

```text
+------------------+   +------------------------------------------+
| Application      |   | calibre-invoker                          |
|                  |-->|  Invoker (DefaultInvoker)                |
| InvocationRequest|   |    | getCommandLineBuilder(request)      |
| (options/goals)  |   |    v                                    |
|                  |   |  AbstractCommandLineBuilder              |
|                  |   |    | build(request)                      |
|                  |   |    v                                    |
+------------------+   |  Commandline (plexus-utils)             |
                      +-------------------+----------------------+
                                          |
                                          v
                     +-------------------------------------------+
                     | Calibre CLI child process (ebook-convert,  |
                     | web2disk, ...)                            |
                     +-------------------+----------------------+
                                          |
                                          v
                     +-------------------------------------------+
                     | InvocationResult (exit code, execution    |
                     | exception)                                |
                     +-------------------------------------------+
```

Single-module Maven project (`packaging: jar`). No child modules.

| Artifact | Responsibility |
| :--- | :--- |
| `io.github.easy4j:calibre-invoker` | Facade (`Invoker`), invocation requests, command line builders, result model |

Key packages:

| Package | Content |
| :--- | :--- |
| `io.github.easy4j.calibre.invoker` | `Invoker`, `DefaultInvoker`, `InvocationResult`, output handlers, loggers |
| `io.github.easy4j.calibre.invoker.request` | `InvocationRequest` + typed `Default*InvocationRequest` classes |
| `io.github.easy4j.calibre.invoker.command` | `AbstractCommandLineBuilder` + per-command builders |
| `io.github.easy4j.calibre.invoker.exception` | `CalibreInvocationException`, `CommandLineConfigurationException` |

## 5. Installation

The project is **not yet published to Maven Central**. Snapshots/releases are
distributed through the Aliyun Maven repository and GitHub Releases.

Maven:

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>calibre-invoker</artifactId>
    <version>1.0.x.20260630-SNAPSHOT</version>
</dependency>
```

Gradle:

```groovy
implementation 'io.github.easy4j:calibre-invoker:1.0.x.20260630-SNAPSHOT'
```

## 6. Quick Start

Download a website with `web2disk` (adapted from the checked-in test):

```java
import io.github.easy4j.calibre.invoker.*;
import io.github.easy4j.calibre.invoker.exception.CalibreInvocationException;
import io.github.easy4j.calibre.invoker.request.DefaultWeb2diskInvocationRequest;
import io.github.easy4j.calibre.invoker.request.Web2diskInvocationRequest;

import java.io.File;

public class Web2diskDemo {

    public static void main(String[] args) throws CalibreInvocationException {
        Web2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setBaseDirectory(new File("/tmp/web2disk-out"));
        request.setURL("https://www.example.com");
        request.setDelay(0);
        request.setDontDownloadStylesheets(true);
        request.setEncoding("UTF-8");

        Invoker invoker = new DefaultInvoker();
        InvocationResult result = invoker.execute(request);

        System.out.println("ExitCode: " + result.getExitCode());
        System.out.println("ExecutionException: " + result.getExecutionException());
    }
}
```

Expected result: Calibre's `web2disk` runs as a child process; the site is saved
under the base directory, and `result.getExitCode()` returns `0` on success.
When Calibre is not installed, a `CalibreInvocationException` is thrown.

## 7. Configuration

The library has no configuration file. All invocation parameters are carried by the
`InvocationRequest` (or derived from the `Invoker` instance). The Calibre installation
is located as follows (from the `Invoker` javadoc):

| Mechanism | Details |
| :--- | :--- |
| `Invoker.setCalibreHome(File)` | Explicit base directory of the Calibre installation |
| System property `calibre.home` | Discovered when no explicit home is set |
| Environment variable `CALIBRE_HOME` | Fallback discovery |
| Default | Calibre resolved from `PATH` (`DEFAULT_EXECUTABLE = "calibre"`) |

Output capture: set `InvocationOutputHandler`s via `Invoker.setOutputHandler(...)` /
`setErrorHandler(...)`; diagnostics go through the pluggable `InvokerLogger`.

## 8. Core Usage / API

### 8.1 The `Invoker` facade

```java
Invoker invoker = new DefaultInvoker();
invoker.setCalibreHome(new File("/Applications/calibre.app/Contents/MacOS")); // optional
invoker.setWorkingDirectory(new File("/tmp"));
invoker.setLogger(new SystemOutLogger());
invoker.setOutputHandler(new PrintStreamHandler(System.out));
```

### 8.2 Typed requests and goals

Each Calibre command has a typed request. `InvocationRequest` also exposes the
low-level `setGoals(List<String>)` / `setVerbose(boolean)` /
`setShellEnvironmentInherited(boolean)` / `addShellEnvironment(name, value)` knobs:

```java
Web2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
request.setGoals(java.util.Arrays.asList(
        "--base-dir=/tmp/site", "--delay=0", "--dont-download-stylesheets",
        "--encoding=UTF-8", "--max-files=20184", "--max-recursions=2", "--timeout=20"));
```

## 9. Testing & Build

```bash
./mvnw clean verify
```

- The build is configured with the JaCoCo Maven plugin (report + `check` goal with a
  90% line-coverage rule bound to the `verify` phase; `haltOnFailure=false`).
- The checked-in test `CalibreInvoker_Web2disk_Test` requires a working Calibre
  installation and a real destination directory; adapt the paths before running.
- No CI workflow files are present under `.github/` in this worktree.

## 10. Versioning & Branches

| Branch | JDK | Version | Notes |
| :--- | :--- | :--- | :--- |
| `feature/1.0.x` | 8 | `1.0.x.*` | Current branch, JDK 8 baseline, maintained |
| `feature/2.0.x` | 17 | `2.0.x.*` | JDK 17 line |
| `feature/3.0.x` | 21 | `3.0.x.*` | JDK 21 line |

Maintenance policy: the `1.0.x` line receives bug fixes and compatibility updates
for the JDK 8 baseline. New features targeting newer JDKs land on the `2.0.x` /
`3.0.x` lines. Releases are published to the Aliyun Maven repository and as
GitHub Releases; the project is not yet published to Maven Central.

## 11. Contributing & License

Contributions are welcome — please open issues or pull requests on GitHub.

Licensed under the [Apache License, Version 2.0](LICENSE).
