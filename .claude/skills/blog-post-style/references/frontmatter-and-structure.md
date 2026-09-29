# Front matter, chemins et i18n

## Où va l'article

- FR (défaut) : `content/posts/YYYY/slug/index.adoc`
- FR, article de série : `content/posts/YYYY/serie/slug/index.adoc`
- EN : mêmes formes sous `content/posts-en/YYYY/slug/index.adoc` ou `content/posts-en/YYYY/serie/slug/index.adoc`

Assets (images, gifs, svg, snippets Java à télécharger...) vivent à côté de `index.adoc`, dans le même dossier, référencés par leur nom de fichier nu : `image::title.webp[]`, `link:MvnDepsTui.java[MvnDepsTui.java]`.

## Profondeur de l'include

Compter les niveaux de dossier depuis la racine du repo jusqu'au dossier de l'article, c'est le nombre de `../` avant `includes/attributes-*.adoc[]`.

- `content/posts/2025/jreleaser/index.adoc` → 4 niveaux → `include::../../../../includes/attributes-fr.adoc[]`
- `content/posts/2025/openrewrite/passing-message/index.adoc` (nested) → 5 niveaux → `include::../../../../../includes/attributes-fr.adoc[]`
- Même formule sous `content/posts-en/`.

## Template front matter — FR, flat

```asciidoc
= Titre de l'article
include::../../../../includes/attributes-fr.adoc[]
:description: Punchline ou question, jamais un résumé SEO plat
:page-tags: tag1,tag2,tag3
:page-image: nom-image.webp
:page-author: jtama
:page-date: YYYY-MM-DD
:page-key: slug-stable-utilise-aussi-pour-lier-la-version-en
```

## Template front matter — FR, nested (série)

```asciidoc
= Titre de l'article
include::../../../../../includes/attributes-fr.adoc[]
:description: ...
:page-tags: ...
:page-image: ...
:page-author: jtama
:page-date: YYYY-MM-DD
:page-key: ...
:page-series: openrewrite
```

## Template front matter — EN, flat

```asciidoc
= Article title
include::../../../../includes/attributes-en.adoc[]
:description: ...
:page-tags: ...
:page-image: ...
:page-author: jtama
:page-date: YYYY-MM-DD
:page-key: meme-cle-que-la-version-fr-si-jumelle
```

## Champs cœur vs optionnels

- **Toujours présents** : `:description:`, `:page-tags:`, `:page-author: jtama`, `:page-image:`, `:page-date:`, `:page-key:`.
- **Situationnels** : `:page-series:` (article appartenant à une série visible, ex. `openrewrite`, `roq`), `:page-lang:` (override seulement — déjà fixé par l'include).
- **Ne jamais ajouter par défaut** : `:qute: true`, `:content-toc:`, `:page-toc:` — déjà posés par `attributes-fr.adoc`/`attributes-en.adoc`. Ne les redéclarer que si on veut explicitement les surcharger. Note pratique : les articles qui embarquent un diagramme `{#diagram}`/`{#mermaid}` redéclarent parfois `:qute: true` en plus de l'include — si un diagramme ne se rend pas, c'est la première chose à essayer.
- **Rares, à ne pas templater** : `:page-title-contains:`, `:icon-set:far`, `:experimental: true` (nécessaire seulement si usage de `kbd:[...]`), `:page-slug:`.

## `includes/attributes-fr.adoc` (référence, ne pas dupliquer dans un article)

```
:sectanchors: true
:idprefix:
:idseparator: -
:icons: font
:page-layout: post
:page-lang: fr
:qute: true
:content-toc: true
:page-toc: Table des matières
```

## `includes/attributes-en.adoc` (référence)

```
:sectanchors: true
:idprefix:
:idseparator: -
:icons: font
:page-layout: post
:page-lang: en
:page-link: /posts/en/:slug/
:qute: true
:page-toc: Table of contents
```

Différences clés : `page-lang` (fr/en), `attributes-fr.adoc` a `:content-toc: true` (absent côté EN), `attributes-en.adoc` a en plus `:page-link: /posts/en/:slug/` (absent côté FR).

## i18n : lier une version FR et une version EN

- `:page-key:` est la clé de pairing entre `content/posts/...` et `content/posts-en/...` : les deux variantes doivent porter exactement la même valeur.
- `:page-lang:` communique la langue au thème (déjà hérité de l'include correspondant — ne le déclarer explicitement que si situation particulière).
- Exemple réel (`content/posts/2026/roq/multilingual-posts/index.adoc`) :

```asciidoc
= Vos articles en mode multilangue avec ROQ
include::../../../../../includes/attributes-fr.adoc[]
:description: Parce qu'écrire en français c'est bien, mais être lu en anglais, c'est pas mal aussi
:page-tags: roq,i18n
:page-image: photo-1533709475520-a0745bba78bf.webp
:page-author: jtama
:page-date: 2026-02-18
:page-key: roq-i18n
:page-lang: fr
```

Une variante EN de ce même article vivrait sous `content/posts-en/...` avec `:page-key: roq-i18n` identique et `:page-lang: en` (via `attributes-en.adoc`).

## Liens internes — forme canonique

```asciidoc
link:{=site.page('posts/2025/mvn-deps/index.adoc').url}[pour savoir d'où sort la version d'une dépendance Maven]
```

- Toujours ce format Qute `link:{=site.page('posts/YYYY/.../index.adoc').url}[label]`, jamais une URL relative brute type `link:../openrewrite-refactoring-as-code[...]` (utilisé une fois dans `content/posts/2025/openrewrite/passing-message/index.adoc:16`, mais ce n'est pas la convention à suivre).
- Ne jamais omettre le `=` : la version FR (`content/posts/2026/operenwrite/project-graph-generator/index.adoc:147`) utilise bien `link:{=site.page(...)`, mais son jumeau EN (`content/posts-en/2026/openrewrite/project-graph-generator/index.adoc:148`) contient un typo (`link:{site.page(...)` sans `=`) — ne pas le reproduire.
