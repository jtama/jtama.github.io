import hljs from 'highlight.js';
import 'highlight.js/scss/github.scss';
import '@catppuccin/palette/css/catppuccin.css'
import mermaid from 'mermaid/dist/mermaid.esm.min.mjs';

hljs.highlightAll();
mermaid.initialize({ startOnLoad: true });
