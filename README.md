Starbox [![](https://jitpack.io/v/Chevyself/starbox-commands.svg)](https://jitpack.io/#Chevyself/starbox-commands)
===

Starbox is a project that provides an abstract interface to extend across different platforms. Currently, it supports the following: Bukkit, Bungee and JDA.

The main objective of Starbox is to simplify the process of creating commands and managing commands. With Starbox, You can easily customize commands using middlewares, providers and other managing options. It enables to write complex commands with minimal effort. 

Starbox utilizes object injection to invoke methods, which rely on providers: `ArgumentProvider` and `ExtraArgumentProvider`. The former requires a `String` input from the command sender. Command execution and result interception can be archived using a `Middleware`. Both providers and middlewares are registered in their respective registries: `ProvidersRegistry`and `MiddlewaresRegistry`. For each platform, Starbox provides a set of middlewares and providers which can be enabled or disabled using the `CommandManagerBuilder`.

If you prefer an alternative approach to reflection, you can implement the interface provided by each module and register it using `CommandManager#regiser(StarboxCommand)`.

 ---

Contents
--------

1. [Installation](#installation)
   1. [Maven](#maven)
   2. [Gradle](#gradle)
2. [Documentation](#documentation)
   1. [JavaDoc](#javadoc)
   2. [Tutorials](#tutorials)
3. [Getting Started](#getting-started)
4. [Platforms](#platforms)
5. [Contribution](#contribution)
6. [License](#license)

---

Installation
--------

## Maven

Add the repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Then, add the dependency:

```xml
<dependency>
    <groupId>com.github.chevyself.starbox-commands</groupId>
    <artifactId>MODULE</artifactId>
    <version>VERSION</version>
</dependency>
```

## Gradle

Add the repository to your `build.gradle`:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then, add the dependency:

```groovy
dependencies {
    implementation 'com.github.chevyself.starbox-commands:MODULE:VERSION'
}
```

> Replace `MODULE` with the module you want to use. For example, `bukkit`, `bungee`, `core`, `jda`.
> 
> Replace `VERSION` with the version you want to use. For example, `master-SNAPSHOT` to use the latest commit.
> 
> Check the [Jitpack](https://jitpack.io/#Chevyself/starbox) page for more information.

---

Documentation
--------

## JavaDoc

Check latest JavaDoc in [Jitpack](https://jitpack.io/com/github/chevyself/starbox-commands/master-SNAPSHOT/javadoc/)

## Tutorials

You can find tutorials in the [Wiki](https://github.com/Chevyself/Starbox-commands/wiki).

Some good tutorials to start with:
1. [Creating a command](https://github.com/Chevyself/starbox-commands/wiki/Creating-Commands)
2. [Creating a middleware](https://github.com/Chevyself/starbox-commands/wiki/Creating-Middlewares)
3. [Creating a provider](https://github.com/Chevyself/starbox-commands/wiki/Creating-Providers)

---

Getting Started
--------

1. To get started, first select the platform you want to use and create the `Adapter`:

* Bukkit: `BukkitAdapter`
* Bungee: `BungeeAdapter`
* JDA: `JDAAdapter`

For this example we will use `BukkitAdapter`:

```java
BukkitAdapter adapter = new BukkitAdapter(plugin, true);
```

> Get more in depth information about the `Adapter` in the [Wiki](./wiki/Adapters).

2. Then, create the `CommandManager` using the `CommandManagerBuilder`:

```java
CommandManager<CommandContext, BukkitCommand> commandManager = new CommandManagerBuilder<>(adapter)
    .build();
```

> Get more in depth information about the `CommandManagerBuilder` in the [Wiki](./wiki/Command_Manager_Builder).

Finally, register the commands:

3. Create and register commands!

Using reflection:
```java
commandManager.parseAndRegister(new MyCommand());
```

Using the `BukkitCommand` interface:

```java
commandManager.register(new MyCommand());
```

> Get more in depth information about the `Commands` in the [Wiki](./wiki/Commands).

---

Platforms
--------

You are advised to view the platforms used in the framework.

* [Spigot](https://hub.spigotmc.org/)
* [BungeeCord](https://github.com/SpigotMC/BungeeCord)
* [JDA](https://github.com/DV8FromTheWorld/JDA)

Contribution
--------
To contribute, simply, branch out of the `master` and create a pull request. If you want to add a new feature, please, create an issue first.

Deprecated content will be removed in the next major release.

All the changes above are in favor to reduce duplicated code and make it easier to maintain.

License
--------

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details