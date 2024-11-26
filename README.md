# Create: Magics
*Note: This is unfinished project, and currently undergoing development*

This mod is meant to add blocks which would make immersive combination of two mods
- Mana and Artifice
- Create


if you wanna build on top of it, you could either fork the repository or import it with cursemaven the file that you need, but besides it, you need to import Create and Mana & Artifice, and their own dependencies, the example of importing those are shown in
build.gradle
but for this mod itself its done with
```groovy
dependencies {
  implementation fg.deobf("curse.maven:create-magics-${create-magics-project-id}:${create-magics-file-id}")
}
```
