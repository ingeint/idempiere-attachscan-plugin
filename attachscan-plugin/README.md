# Attachscan Plugin

- Copyright: 2020 INGEINT <https://www.ingeint.com>
- Repository: https://github.com/ingeint/idempiere-attachscan-plugin
- License: GPL 2

## Description

This plugin implement the interface 'sane' to scan directly from iDempiere.

## Components

- iDempiere Plugin [com.ingeint.attachscan](com.ingeint.attachscan)
- iDempiere Unit Test Fragment [com.ingeint.attachscan.test](com.ingeint.attachscan.test)
- iDempiere Target Platform [com.ingeint.attachscan.targetplatform](com.ingeint.attachscan.targetplatform)

## Prerequisites

- Java 11, commands `java` and `javac`.
- iDempiere 7.1.0
- Set `IDEMPIERE_REPOSITORY` env variable

## Commands

Compile plugin and run tests:

```bash
./build
```

Use the parameter `debug` for debug mode example:

```bash
./build debug
```

To use `.\build.bat` for windows.
