# Voix et ton

## Langue et franglais

- Un article reste monolingue de bout en bout (jamais de code-switch phrase à phrase), mais le vocabulaire technique anglais est inséré librement, en italique quand c'est un anglicisme assumé : `_dry run_`, `_stream_`, `_legacy_`, `_fwk_`.
- L'auteur invente ou détourne des mots à partir de l'anglais sans complexe : "fourchettez-le" (forker), "kassdédi" (dédicace), "woké" (okay). Ça ne se justifie jamais dans le texte — c'est juste utilisé, avec confiance.
- Parfois une blague meta sur son propre franglais : le titre `TamboUI, prononcé _tambouille_, excuse my french` (`content/posts/2026/tamboui/index.adoc:1`) joue sur le mélange lui-même.
- Traduction bilingue en écho : citer un slogan/terme anglais puis le reformuler en français juste après, plutôt que choisir l'un ou l'autre.

## Adresse au lecteur et questions rhétoriques

- "vous" domine (poli mais pas distant), "on"/"nous" quand on construit le code ensemble avec le lecteur.
- Accroche d'ouverture typique : callback à un article précédent, avec lien, plutôt qu'un exposé du sujet. Exemple réel (`content/posts/2026/tamboui/index.adoc:12`) :

  > "Il y a un peu plus d'un an, je vous avais donné une astuce link:{=site.page('posts/2025/mvn-deps/index.adoc').url}[pour savoir d'où sort la version d'une dépendance Maven] [...]"

- Questions rhétoriques utilisées comme accroche de section ou relance après une explication (`passing-message/index.adoc:16`) : "C'est bon ? Allez go."
- Une objection du lecteur peut être mise en scène directement (voir `asciidoc-patterns.md` → dispositif "lecteur curieux"), puis désamorcée par l'auteur en "je"/"tu" complice : "Oui, je te comprends, et cette question m'a moi aussi empêché de dormir pendant quelques jours."

## Humour : où et combien

- Placé aux endroits stratégiques : titre, titres de section, accroche d'ouverture, transitions, clôture — jamais forcé à chaque paragraphe technique. Une explication de mécanique interne (ex. gestion d'événements) reste sérieuse et précise ; l'humour encadre, il ne pollue pas l'explication elle-même.
- Un aparté potache est acceptable en plein milieu d'un point technique, tant qu'il tient en une phrase et ne casse pas le fil : "(non mais sérieusement, il claque pas trop ce nom ?)" (`tamboui/index.adoc:29`).
- Jeux de mots/références culturelles bienvenus dans les titres (Shakespeare, Kaamelott, Star Wars, cinéma...) mais pas obligatoires à chaque section — 1 à 2 par article suffisent.
- Un aveu de fainéantise ou d'erreur assumée fait partie du registre : "Woké, si c'est comme ça qu'il faut faire, je le fais." (`tamboui/index.adoc:198`).
- Emoji/emoticons sobres et placés au bon endroit, jamais décoratifs : le shrug `¯\_(ツ)_/¯` revient plusieurs fois dans le corpus pour marquer une résignation ironique (`passing-message/index.adoc:159`). Un 💀 ou un ❤️ en fin de phrase pour ponctuer une blague, pas plus d'un ou deux par article.

## Avis personnels

- Assumés sans fausse neutralité, y compris en admettant une lacune : "Personnellement, j'ai choisi...", "Moi non." — l'auteur n'hésite pas à dire qu'il ne sait pas quelque chose plutôt que de noyer le lecteur dans du vague.
- Rendre à César : créditer explicitement l'inspiration/le travail d'un tiers plutôt que de s'en attribuer le mérite — "Ou pour rendre à César ce qui appartient à César : https://xam.dk/[Max Andersen] a bien fait les choses." (`tamboui/index.adoc:306`).

## Dédicaces

- En fin d'article, une personne réelle nommée + lien (LinkedIn, GitHub...) + parfois un emoji : "Spécial kassdédi https://www.linkedin.com/in/thierrychantier/[Thierry Chantier]." (`tamboui/index.adoc:15`), "Spécial kassdédi à Lucile Thiénot et Florian Gomas ❤️!" (`content/posts/2025/oop-to-dop/index.adoc`).
- Toujours factuel et chaleureux, jamais ironique envers la personne citée (l'ironie/l'autodérision reste dirigée vers soi-même ou le sujet technique).

## Formules de clôture

- Les deux formes sont attestées et valables : un titre créatif ("Et woilà \o/ !", "Pour conclure", "On y va ou pas ?", "Retour à la case départ") ou un `== Conclusion` littéral (`tamboui/index.adoc:315` l'utilise directement). Ce qui compte n'est pas l'intitulé mais le contenu : jamais une clôture vide de sens — elle doit apporter un recap (2-3 points clés) et/ou un lien vers le dépôt/les ressources. Le seul vrai défaut à éviter, c'est un `== Conclusion` qui ne fait que répéter platement l'intro sans rien ajouter.
- Formule d'humilité récurrente sur la portée de l'article : "je n'ai fait que gratter la surface" / "I've only scratched the surface" — à réutiliser ou varier, pas à copier mot pour mot systématiquement.
- Presque toujours suivi d'un lien vers le dépôt GitHub complet, avec invitation à l'essayer/le forker/contribuer : "Allez-y c'est open source, utilisez-le, fourchettez-le, faîtes donc des tickets et des demandes de tirage!"
- Un recap en 2-3 bullet points est un bon substitut/complément à une conclusion en prose : voir `tamboui/index.adoc:315-320` ("Deux choses à retenir :").
- Un P.S. en toute fin est acceptable pour recommander un autre article ou une ressource tangentielle (cross-promotion assumée), voir `content/posts/2026/operenwrite/project-graph-generator/index.adoc`.
- Une section `=== Ressources` en toute fin (liens GitHub, doc officielle, articles liés) est un bon complément à la conclusion, voir `tamboui/index.adoc:322-327`.

## Ce qu'il ne faut pas faire

- Pas d'ouverture "Dans cet article je vais parler de X" — toujours une accroche (question, callback, anecdote, affirmation).
- Pas d'humour permanent qui noie l'explication technique — un article reste avant tout précis et utile.
- Pas de fausse neutralité corporate ("cette approche présente des avantages et des inconvénients à considérer") — préférer un avis tranché assumé.
- Pas de clôture sans lien vers le code/les ressources.
