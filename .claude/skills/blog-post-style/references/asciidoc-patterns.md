# Dispositifs AsciiDoc et syntaxe exacte

## TL;DR

Optionnel, juste après le front matter (avant même l'image de couverture si besoin), ton potache pour les pressés. Exemple réel (`content/posts/2026/tamboui/index.adoc:18-24`) :

```asciidoc
== TL;DR;

https://tamboui.dev/[TamboUI] c'est bien. Si vous avez une TUI à implémenter, donnez-lui sa chance.

Merci, au revoir.

image::tamboui_logo.svg[]
```

## Quiz avec réponse cachée (collapsible)

Poser une question/un défi au lecteur, cacher la réponse dans un bloc collapsible pour qu'il essaie avant de vérifier :

```asciidoc
[%collapsible]
====
Réponse : ...
====
```

Utilisé notamment dans `content/posts/2025/gatherer/index.adoc` (3 quiz successifs) et `content/posts/2025/oop-to-dop/index.adoc`.

## Citation fictive du "lecteur curieux"

Mettre en scène une objection anticipée du lecteur en `[quote]`, puis y répondre directement. Syntaxe exacte réelle (`content/posts/2025/openrewrite/passing-message/index.adoc:43-46`) :

```asciidoc
[quote, Lecteur attentif et curieux]
Oui, mais si mon cas d'usage nécessite d'identifier [.bg-green]#une invocation de méthode# pour agir sur [.bg-mauve]#la déclaration de la méthode# qui la contient dans [.bg-rosewater]#une classe#?

Oui, je te comprends, et cette question m'a moi aussi empecher de dormir pendant quelques jours. [...]
```

Une variante existe avec attribution comique différente, par ex. `[quote, Écrit noir sur blanc dans le commentaire de la classe...]` (même fichier, ligne 195) pour citer une source réelle (commentaire de code) sur le même mode théâtral.

## Before / After

Deux sous-sections ou deux blocs de code consécutifs montrant une transformation, souvent titrées `=== Avant` / `=== Après` (voir `content/posts/2025/oop-to-dop/index.adoc`) :

```asciidoc
=== Avant

[source,java]
----
// version couplée / impérative
----

=== Après

[source,java]
----
// version découplée / idiomatique
----
```

## Admonitions

- `[NOTE]` : complément d'information non bloquant.
- `[TIP]` : astuce pratique ("pensez à mettre à jour telle version de plugin...").
- `[IMPORTANT]` : attribution/crédit, ou point que le lecteur ne doit pas manquer.
- `[WARNING]` : sujet avancé ou piège à anticiper avant de continuer.
- `[CAUTION]` : mise en garde sur un comportement contre-intuitif du code montré juste avant/après.

Syntaxe avec délimiteur (permet plusieurs paragraphes/blocs à l'intérieur), exemple réel (`content/posts/2026/tamboui/index.adoc:46-50`) :

```asciidoc
[NOTE]
====
Quoiqu'il arrive, le _fwk_ offre une bibliothèque de _widgets_, des primitives de layout [...]
Vous avez aussi en cadeaux l'adaptation à la taille du terminal même sur du resizing.
====
```

## Callouts numérotés

Chaque `<n>` dans un bloc de code est expliqué dans la liste numérotée juste en dessous. Exemple canonique complet (`content/posts/2026/tamboui/index.adoc:292-298`) :

```asciidoc
[source,java]
----
InputLocation location = managed.getLocation("version"); <1>
managedByLine = location.getLineNumber();
managedByModelId = location.getSource() != null ? location.getSource().getModelId() : null;
----
<1> Maven trace, pour chaque champ de chaque modèle qu'il fusionne (parent, BOM importé, projet courant), sa source exacte [...]
```

Une explication peut légitimement être elle-même longue/technique (pas juste un label d'une ligne) tant qu'elle apporte une info que le code seul ne donne pas — voir l'exemple ci-dessus. Un callout peut aussi être volontairement trivial pour l'humour ("Non, mais vous avez sérieusement pensé que j'allais expliquer cette ligne ?" — `content/posts/2026/tamboui/index.adoc:223`).

## Diagrammes Qute (Mermaid / PlantUML)

Syntaxe réelle confirmée pour un diagramme PlantUML (mindmap ou activité), via la balise Qute du thème (`content/posts/2026/tamboui/index.adoc:179-190`) :

```asciidoc
{#diagram asciidoc=true language="plantuml" alt="Activity" width=500 height=500 diagramOutputFormat="svg"}
@startmindmap
#[#f2d5cf] ""/""
 #[#ca9ee6] Saisit une requête
  #[#a6d189] ""↵""
@endmindmap
{/}
```

Ou pour un diagramme d'activité (`content/posts/2025/openrewrite/passing-message/index.adoc:165-184`) :

```asciidoc
{#diagram asciidoc=true language="plantuml" alt="Diagramme d'activité" width=442 height=718 diagramOutputFormat="svg"}
@startuml
!theme bluegray
start
:Une étape;
if (Condition) then (oui)
  :Action;
endif
stop
@enduml
{/}
```

Nécessite `:qute: true` — déjà hérité de l'include, ne pas le redéclarer sauf besoin explicite. Mermaid suit la même logique via `{#mermaid}...{/}` (voir `content/posts/2025/roq/mermaid/index.adoc`).

## Crédit photo (photos de stock)

Deux formes réelles, au choix :

- Footnote juste après le paragraphe qui introduit l'image (`content/posts/2025/openrewrite/passing-message/index.adoc:13-14`) :

  ```asciidoc
  __Photo par Pixabay.footnote:[https://www.pexels.com/photo/water-drops-on-blue-background-260551/[Pixabay]]
  __
  ```

- Ligne de crédit inline en fin d'article : "Illustration par https://unsplash.com/@handle[Nom]".

## Habitudes de mise en forme

- `+` en fin de ligne pour forcer un saut de ligne doux à l'intérieur d'un paragraphe (rythme de lecture), très fréquent : "[...] suivi d'un `grep`. +\nÇa marche, mais c'est un peu rude [...]" (`tamboui/index.adoc:12-13`).
- `{empty} +` sur sa propre ligne pour forcer un saut vertical/aération entre deux blocs (avant/après un diagramme par exemple).
- Gras (`*mot*`) pour les termes clés/produits à leur première mention (`*TamboUI*`).
- Italique (`_mot_`) pour les anglicismes/jargon ou une emphase ironique (`_comme il faut_`).
- Code inline (`` `mot` ``) très dense, jusque dans des phrases ordinaires — ne pas hésiter à taguer noms de classes/méthodes/CLI même en plein milieu d'une phrase.
- Listes horizontales `[horizontal]` avec `terme:: description` pour présenter des options/niveaux de façon compacte plutôt qu'en prose (`tamboui/index.adoc:41-44`).
