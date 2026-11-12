Draconic-Evolution
==================

Draconic Evolution adds powerful new tools, Items, weapons and Armor. The premise is the enigmatic ore - Draconium. As you progress you find it has many uses from the mundane such as machines and items, to the near-mystical at the top with Awakened Draconium to make extremely powerful armor and tools.

## License / Use in Modpacks
This mod is licensed under the [**Don't Be a Jerk License**](https://github.com/brandon3055/Draconic-Evolution/blob/master/LICENSE) created by CoFH.
I herby grant permission to use this mod in any mod pack without the need to request permission from myself the owner (brandon3055).

## Development

Requires Java 8. Gradle runs through the wrapper.

| Command | What it does |
|---|---|
| `./gradlew build` | Compiles the mod and runs Checkstyle and PMD. |
| `./gradlew format` | Rewrites all Java sources with google-java-format 1.7 (AOSP style). |
| `./gradlew formatCheck` | Fails if a Java source is not formatted. |
| `./gradlew checkstyleMain pmdMain` | Runs only the linters. Reports are in `build/reports/`. |

- Checkstyle rules are in `config/checkstyle/checkstyle.xml`. They check code patterns, not layout: the formatter owns layout.
- `config/checkstyle/suppressions.xml` is a baseline of the violations that existed when the linter was added. New code must comply. Remove entries when you fix a file.
- PMD rules are in `config/pmd/ruleset.xml`. PMD reports only for now; it does not fail the build.
