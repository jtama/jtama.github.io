---
name: blog-post-style
description: Use when writing a new blog post or editing/reviewing an existing one under content/posts or content/posts-en in this repo (jtama.github.io, Quarkus Roq, AsciiDoc articles). Ensures the article matches the author's voice (franglais assumé, autodérision, adresse directe au lecteur), sa structure habituelle (TL;DR, quiz en collapsible, callouts numérotés, before/after, formule de clôture) et les conventions front-matter/i18n du repo. Déclencheurs typiques - "écrire un article", "créer un post de blog", "rédiger un article", "relire/retoucher un article existant".
---

# Blog post style (jtama.github.io)

Ce blog technique n'a qu'une seule voix d'auteur, à préserver sur tous les articles : AsciiDoc uniquement, ton direct, franglais assumé, humour d'autodérision, dispositifs pédagogiques récurrents (quiz, before/after, callouts). Ce skill sert à la fois de rulebook structurel et de guide de voix.

## Deux modes d'usage

- **Nouvel article** → lire entièrement `references/frontmatter-and-structure.md` avant d'écrire quoi que ce soit (chemin, front matter, i18n).
- **Retouche d'un article existant** → règle de l'intervention minimale : corriger la mécanique/structure et signaler les écarts de style, ne pas réécrire la prose existante de l'auteur en profondeur. Revérifier avec la checklist finale sans regénérer les sections inchangées.

## Chemins, en un coup d'œil

- FR : `content/posts/YYYY/slug/index.adoc`, ou nested `content/posts/YYYY/serie/slug/index.adoc`.
- EN : même forme sous `content/posts-en/`.
- Le nombre de `../` avant `includes/attributes-*.adoc` = nombre de niveaux de dossiers depuis la racine du repo jusqu'au dossier de l'article.
- Template front matter complet, formule de profondeur, et pairing FR/EN via `:page-key:` → voir `references/frontmatter-and-structure.md`.

## Règles mécaniques non négociables

- AsciiDoc uniquement, jamais Markdown.
- Ne jamais déclarer `:qute: true`, `:content-toc:` ou `:page-toc:` par article — déjà hérités de l'include `attributes-*.adoc`.
- Liens internes uniquement sous la forme `` link:{=site.page('posts/YYYY/.../index.adoc').url}[label] `` — jamais une URL relative brute, jamais sans le `=` (un typo existant dans le repo à `content/posts/2026/operenwrite/project-graph-generator/index.adoc:147` ne doit pas être imité).
- Chaque bloc `[source,xxx]` déclare un langage explicite.
- Chaque callout numéroté `<n>` dans le code est expliqué dans la liste juste en dessous.
- Les photos de stock sont créditées (footnote ou ligne de crédit inline) — voir `references/asciidoc-patterns.md`.

## Voix, en condensé

| Élément | Règle | Exemple court |
|---|---|---|
| Langue/franglais | Assumé, parfois moqué par l'auteur lui-même ; jamais de code-switch en pleine phrase — un article reste monolingue | "je préfère faire uniquement des _dry run_ localement" |
| Adresse | "vous" dominant, "on"/"nous" pour narrer le code ensemble | "Voici la commande pour lancer une analyse complète de votre projet :" |
| Humour | Placé aux titres, accroches, transitions, clôture — jamais forcé à chaque paragraphe | jeux de mots dans les titres, shrug `¯\_(ツ)_/¯` |
| Avis personnels | Assumés sans fausse neutralité, y compris avouer ne pas savoir | "Personnellement, j'ai choisi...", "Moi non." |
| Dédicaces | Personne réelle nommée + lien, en fin d'article | "Spécial kassdédi à Lucile Thiénot et Florian Gomas ❤️!" |
| Clôture | Titre créatif ou "Conclusion" littéral, peu importe — mais jamais vide : recap + lien dépôt/ressources | "Et woilà \o/ !", "je n'ai fait que gratter la surface" |

Guide complet avec extraits réels cités → `references/voice-and-tone.md`.

## Dispositifs structurels, en condensé

- `== TL;DR;` optionnel juste après le front matter, ton potache, pour les pressés.
- Quiz pédagogique avec réponse cachée en `[%collapsible]`.
- Citation fictive d'un "lecteur curieux" en `[quote, ...]` pour anticiper une objection.
- Blocs Before/After pour montrer une transformation de code.
- Admonitions `[NOTE]`/`[TIP]`/`[IMPORTANT]`/`[WARNING]`/`[CAUTION]` pour les mises en garde.
- Diagrammes Mermaid/PlantUML via la syntaxe Qute du thème (`{#diagram}`).

Syntaxe exacte de chaque dispositif → `references/asciidoc-patterns.md`.

## À ne jamais faire

- Ouvrir par "Dans cet article je vais parler de...".
- Une clôture vide qui ne fait que répéter l'intro, sans recap ni lien vers le dépôt/les ressources (le titre "Conclusion" en lui-même est acceptable, voir `references/voice-and-tone.md`).
- Un `:description:` plat façon résumé SEO au lieu d'une punchline/question.
- De la syntaxe Markdown, où que ce soit.
- Une clôture sans lien vers le dépôt/les ressources.

## Checklist finale

- [ ] Chemin et nom de fichier conformes (flat vs nested, FR vs EN).
- [ ] Front matter complet (champs cœur présents), profondeur d'include correcte, pas de `:qute:`/`:content-toc:` redondant.
- [ ] Si une version FR/EN jumelle existe ou est prévue, `:page-key:` identique et `:page-lang:` correct.
- [ ] Chaque lien interne utilise la forme canonique `site.page(...)`.
- [ ] Chaque bloc `[source,...]` a un langage ; chaque callout `<n>` est expliqué.
- [ ] Au moins un dispositif structurel présent là où il a du sens (pas forcé partout).
- [ ] Titre et `:description:` ne sont pas plats/descriptifs.
- [ ] La clôture apporte un recap et/ou un lien dépôt/ressources (le titre "Conclusion" est acceptable, une clôture vide ne l'est pas).
- [ ] Photos de stock créditées.
- [ ] Franglais/ton cohérents avec le reste du corpus, article monolingue.
