import hljs from 'highlight.js';
import 'highlight.js/scss/github.scss';
import '@catppuccin/palette/css/catppuccin.css'
import mermaid from 'mermaid/dist/mermaid.esm.min.mjs';
import '@fortawesome/fontawesome-free/scss/fontawesome.scss';
import '@fortawesome/fontawesome-free/scss/regular.scss';
import '@fortawesome/fontawesome-free/scss/brands.scss';
import '@fortawesome/fontawesome-free/scss/solid.scss';

hljs.highlightAll();
mermaid.initialize({ startOnLoad: true });
