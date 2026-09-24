(function () {
    const toc = document.querySelector('.guide-toc-wrapper .toc');
    if (!toc) {
        return;
    }

    const OFFSET = 120;

    const groups = [];
    const groupByHeadingId = {};
    const headings = [];

    function addToggle(li) {
        const link = li.querySelector(':scope > a');
        if (!link) {
            return;
        }
        const row = document.createElement('span');
        row.className = 'toc-row';
        link.replaceWith(row);

        const button = document.createElement('button');
        button.type = 'button';
        button.className = 'toc-toggle';
        button.setAttribute('aria-expanded', 'false');
        button.setAttribute('aria-label', 'Afficher ou masquer les sous-sections');
        row.appendChild(button);
        row.appendChild(link);

        button.addEventListener('click', function () {
            const isOpen = !li.classList.contains('is-open');
            setGroupOpen(li, isOpen);
            if (!isOpen && li === openGroup) {
                openGroup = null;
            }
        });
    }

    const topItems = toc.querySelectorAll('.roq-toc > ul > li');
    topItems.forEach(function (li) {
        const hasChildren = !!li.querySelector('ul');
        if (hasChildren) {
            li.classList.add('toc-group');
            groups.push(li);
            addToggle(li);
        }
        li.querySelectorAll('a[href^="#"]').forEach(function (link) {
            const id = decodeURIComponent(link.getAttribute('href').slice(1));
            const target = id && document.getElementById(id);
            if (!target) {
                return;
            }
            headings.push({id: id, el: target, link: link});
            groupByHeadingId[id] = hasChildren ? li : null;
        });
    });

    if (!headings.length) {
        return;
    }

    let openGroup = null;
    let activeLink = null;

    function setGroupOpen(li, isOpen) {
        li.classList.toggle('is-open', isOpen);
        var button = li.querySelector(':scope > .toc-row > .toc-toggle');
        if (button) {
            button.setAttribute('aria-expanded', String(isOpen));
        }
        var subList = li.querySelector(':scope > ul');
        if (subList) {
            subList.style.maxHeight = isOpen ? subList.scrollHeight + 'px' : '0px';
        }
    }

    function apply(heading) {
        var group = heading ? groupByHeadingId[heading.id] : null;
        if (group !== openGroup) {
            if (openGroup) {
                setGroupOpen(openGroup, false);
            }
            if (group) {
                setGroupOpen(group, true);
            }
            openGroup = group;
        }

        var link = heading ? heading.link : null;
        if (link !== activeLink) {
            if (activeLink) {
                activeLink.classList.remove('is-active');
            }
            if (link) {
                link.classList.add('is-active');
            }
            activeLink = link;
        }
    }

    function currentHeading() {
        let current = null;
        for (let i = 0; i < headings.length; i++) {
            if (headings[i].el.getBoundingClientRect().top - OFFSET <= 0) {
                current = headings[i];
            }
            else {
                break;
            }
        }
        return current;
    }

    let ticking = false;

    function onScroll() {
        if (ticking) {
            return;
        }
        ticking = true;
        window.requestAnimationFrame(function () {
            apply(currentHeading());
            ticking = false;
        });
    }

    window.addEventListener('scroll', onScroll, {passive: true});
    apply(currentHeading());
})();
