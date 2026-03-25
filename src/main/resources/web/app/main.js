import hljs from 'highlight.js';
import CopyButtonPlugin from 'highlightjs-copy';
import 'highlightjs-copy/dist/highlightjs-copy.min.css';
import 'highlight.js/scss/github.scss';
import mermaid from 'mermaid/dist/mermaid.esm.min.mjs';
import '@fortawesome/fontawesome-free/scss/fontawesome.scss';
import '@fortawesome/fontawesome-free/scss/regular.scss';
import '@fortawesome/fontawesome-free/scss/brands.scss';
import '@fortawesome/fontawesome-free/scss/solid.scss';

hljs.addPlugin(
    new CopyButtonPlugin({
        hook: (text, _) => {
            return text.replaceAll(/\(\d+\)$/gm, '');
        },
    }
));
hljs.highlightAll();
mermaid.initialize({startOnLoad: true});