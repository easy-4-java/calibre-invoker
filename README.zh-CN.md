# calibre-invoker

[English](./README.md) | [简体中文](./README.zh-CN.md)

[![Java](https://img.shields.io/badge/Java-8-orange)](https://github.com/easy-4-java/calibre-invoker) [![License](https://img.shields.io/badge/license-Apache%202.0-green)](./LICENSE)

> 以编程方式调用 [Calibre](https://calibre-ebook.com) 命令行工具的 Java 封装
> （ebook-convert、ebook-edit、ebook-polish、ebook-viewer、ebook-meta、
> fetch-ebook-metadata、web2disk、lrf2lrs、lrs2lrf 等）。

## 目录

- [1. 项目概述](#1-项目概述)
- [2. 功能与状态](#2-功能与状态)
- [3. 环境要求与兼容性](#3-环境要求与兼容性)
- [4. 架构与模块](#4-架构与模块)
- [5. 安装](#5-安装)
- [6. 快速开始](#6-快速开始)
- [7. 配置](#7-配置)
- [8. 核心用法 / API](#8-核心用法--api)
- [9. 测试与构建](#9-测试与构建)
- [10. 版本与分支](#10-版本与分支)
- [11. 贡献与许可](#11-贡献与许可)

## 1. 项目概述

`calibre-invoker` 是一个小型库，允许 Java 应用以子进程方式启动并控制
[Calibre](https://calibre-ebook.com) 命令行工具。它采用经典的 invoker 模式：
先构建描述选项的 `InvocationRequest`，交给 `Invoker` 执行，再通过
`InvocationResult` 获取进程退出码。

该库只是 Calibre 可执行文件的薄封装——本身**不**实现任何电子书转换逻辑，也**不**
需要运行中的 Calibre 图书馆服务。**系统上必须安装并可运行 Calibre。**

命令行参考以官方
[Calibre CLI 索引](https://manual.calibre-ebook.com/generated/en/cli-index.html) 为准。

典型场景：

| 场景 | 调用请求 |
| :--- | :--- |
| 电子书格式转换 | `DefaultEbookConvertInvocationRequest`（`ebook-convert`） |
| 将网站下载为电子书 / 网页归档 | `DefaultWeb2diskInvocationRequest`（`web2disk`） |
| 编辑 / 打磨电子书元数据 | `DefaultEbookEditInvocationRequest` / `DefaultEbookPolishInvocationRequest` |
| 读取电子书元数据 | `DefaultEbookMetaInvocationRequest` / `DefaultFetchEbookMetadataInvocationRequest`（`ebook-meta`、`fetch-ebook-metadata`） |
| LRF 与 LRS 格式互转 | `DefaultLrf2lrsInvocationRequest` / `DefaultLrs2lrfInvocationRequest` |
| 启动电子书阅读器 | `DefaultEbookViewerInvocationRequest`（`ebook-viewer`） |

## 2. 功能与状态

| 能力 | 状态 | 说明 |
| :--- | :--- | :--- |
| `Invoker` / `DefaultInvoker` 门面 | 稳定 | `execute(InvocationRequest)`、工作目录、Calibre home、日志器与输出 / 错误处理器 |
| 类型化调用请求 | 稳定 | 每个 Calibre 命令对应一个 `Default*InvocationRequest`（convert、edit、meta、polish、viewer、web2disk、fetch-ebook-metadata、lrf2lrs、lrs2lrf） |
| 命令行构建器 | 稳定 | `AbstractCommandLineBuilder` + 各命令构建器组装操作系统命令行 |
| 输出捕获 | 稳定 | 可插拔 `InvocationOutputHandler`（`SystemOutHandler`、`PrintStreamHandler`） |
| 日志 | 稳定 | 可插拔 `InvokerLogger`（`SystemOutLogger`、`PrintStreamLogger`） |
| 退出码结果模型 | 稳定 | `InvocationResult.getExitCode()`、`getExecutionException()` |

## 3. 环境要求与兼容性

| 要求 | 版本 / 说明 |
| :--- | :--- |
| JDK | 8+ |
| Maven | 3.0+（enforcer 强制；项目内置 Maven Wrapper `./mvnw`） |
| Calibre | 必须已安装且在 `PATH` 中，或通过 `calibre.home` 系统属性 / `CALIBRE_HOME` 环境变量定位 |

版本线：

| 分支 | JDK | 版本 |
| :--- | :--- | :--- |
| `feature/1.0.x` | 8 | `1.0.x.*` |
| `feature/2.0.x` | 17 | `2.0.x.*` |
| `feature/3.0.x` | 21 | `3.0.x.*` |

## 4. 架构与模块

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

单模块 Maven 工程（`packaging: jar`），无子模块。

| 构件 | 职责 |
| :--- | :--- |
| `io.github.easy4j:calibre-invoker` | 门面（`Invoker`）、调用请求、命令行构建器、结果模型 |

关键包：

| 包 | 内容 |
| :--- | :--- |
| `io.github.easy4j.calibre.invoker` | `Invoker`、`DefaultInvoker`、`InvocationResult`、输出处理器、日志器 |
| `io.github.easy4j.calibre.invoker.request` | `InvocationRequest` + 类型化的 `Default*InvocationRequest` |
| `io.github.easy4j.calibre.invoker.command` | `AbstractCommandLineBuilder` + 各命令构建器 |
| `io.github.easy4j.calibre.invoker.exception` | `CalibreInvocationException`、`CommandLineConfigurationException` |

## 5. 安装

项目**尚未发布到 Maven Central**。快照 / 发布版本通过阿里云 Maven 仓库与 GitHub
Releases 分发。

Maven：

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>calibre-invoker</artifactId>
    <version>1.0.x.20260630-SNAPSHOT</version>
</dependency>
```

Gradle：

```groovy
implementation 'io.github.easy4j:calibre-invoker:1.0.x.20260630-SNAPSHOT'
```

## 6. 快速开始

使用 `web2disk` 下载网站（改编自仓库内已提交的测试）：

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

预期结果：Calibre 的 `web2disk` 以子进程方式运行，网站被保存到基础目录下，
成功时 `result.getExitCode()` 返回 `0`。未安装 Calibre 时会抛出
`CalibreInvocationException`。

## 7. 配置

本库没有配置文件。所有调用参数都由 `InvocationRequest` 承载（或从 `Invoker`
实例推导）。Calibre 安装位置按以下顺序定位（依据 `Invoker` 的 Javadoc）：

| 机制 | 说明 |
| :--- | :--- |
| `Invoker.setCalibreHome(File)` | 显式指定 Calibre 安装基础目录 |
| 系统属性 `calibre.home` | 未显式设置时自动发现 |
| 环境变量 `CALIBRE_HOME` | 兜底的自动发现 |
| 默认 | 从 `PATH` 解析 Calibre（`DEFAULT_EXECUTABLE = "calibre"`） |

输出捕获：通过 `Invoker.setOutputHandler(...)` / `setErrorHandler(...)` 设置
`InvocationOutputHandler`；诊断信息走可插拔的 `InvokerLogger`。

## 8. 核心用法 / API

### 8.1 `Invoker` 门面

```java
Invoker invoker = new DefaultInvoker();
invoker.setCalibreHome(new File("/Applications/calibre.app/Contents/MacOS")); // 可选
invoker.setWorkingDirectory(new File("/tmp"));
invoker.setLogger(new SystemOutLogger());
invoker.setOutputHandler(new PrintStreamHandler(System.out));
```

### 8.2 类型化请求与 goals

每个 Calibre 命令都有对应的类型化请求。`InvocationRequest` 还提供底层控制项：
`setGoals(List<String>)`、`setVerbose(boolean)`、`setShellEnvironmentInherited(boolean)`、
`addShellEnvironment(name, value)`：

```java
Web2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
request.setGoals(java.util.Arrays.asList(
        "--base-dir=/tmp/site", "--delay=0", "--dont-download-stylesheets",
        "--encoding=UTF-8", "--max-files=20184", "--max-recursions=2", "--timeout=20"));
```

## 9. 测试与构建

```bash
./mvnw clean verify
```

- 构建配置了 JaCoCo Maven 插件（报告 + 绑定在 `verify` 阶段的 `check` 目标，
  行覆盖率规则为 90%；`haltOnFailure=false`）。
- 已提交的测试 `CalibreInvoker_Web2disk_Test` 需要可用的 Calibre 安装与真实的目标
  目录，运行前请调整路径。
- 本 worktree 的 `.github/` 下无 CI 工作流文件。

## 10. 版本与分支

| 分支 | JDK | 版本 | 说明 |
| :--- | :--- | :--- | :--- |
| `feature/1.0.x` | 8 | `1.0.x.*` | 当前分支，JDK 8 基线，维护中 |
| `feature/2.0.x` | 17 | `2.0.x.*` | JDK 17 版本线 |
| `feature/3.0.x` | 21 | `3.0.x.*` | JDK 21 版本线 |

维护策略：`1.0.x` 版本线接收针对 JDK 8 基线的缺陷修复与兼容性更新；面向新 JDK 的
新特性在 `2.0.x` / `3.0.x` 版本线开发。发布物通过阿里云 Maven 仓库与 GitHub
Releases 分发；项目尚未发布到 Maven Central。

## 11. 贡献与许可

欢迎通过 GitHub Issue 或 Pull Request 参与贡献。

本项目基于 [Apache License, Version 2.0](LICENSE) 许可。
