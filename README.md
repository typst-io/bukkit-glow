# bukkit-glow

## Add dependency

In gradle:

groovy
```
compileOnly 'io.typst:bukkit-glow:2.1.1'
```

kts
```
compileOnly("io.typst:bukkit-glow:2.1.1")
```

In plugin.yml:
```yaml
depend:
  - 'BukkitGlow'
```

Put the BukkitGlow plugin in your plugins folder

## Usage

```java
GlowAPI glow = new GlowAPI(); // No cost for construction
glow.setGlowing(receiver, target, color);
glow.removeGlowing(receiver, target);
```

## Support plugins
- [ModelEngine](https://git.mythiccraft.io/mythiccraft/model-engine-4/-/wikis/Modeling)
