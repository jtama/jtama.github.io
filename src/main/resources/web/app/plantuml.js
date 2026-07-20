document.addEventListener('DOMContentLoaded', () => {
    const svg = document.querySelector('svg');
    if (!svg) return;

    const entities = document.querySelectorAll('.entity,.cluster');
    const links = document.querySelectorAll('.link');

    // Build entity IDs set for quick existence check
    const entityIds = new Set(Array.from(entities).map(e => e.id));

    // Build the directed adjacency graph list (only outgoing: from data-entity-1 to data-entity-2)
    const adj = {};
    links.forEach(link => {
        const ent1 = link.getAttribute('data-entity-1');
        const ent2 = link.getAttribute('data-entity-2');
        if (ent1 && ent2 && entityIds.has(ent1) && entityIds.has(ent2)) {
            if (!adj[ent1]) adj[ent1] = [];
            // Only follow outgoing links
            adj[ent1].push({ neighborId: ent2, linkElement: link });
        }
    });

    let activeRootId = null;

    // Helper to clear all selection states
    function clearSelection() {
        svg.classList.remove('has-selection');
        entities.forEach(ent => {
            ent.classList.remove('highlighted', 'active-root');
        });
        links.forEach(lnk => {
            lnk.classList.remove('highlighted');
        });
        activeRootId = null;
    }

    // Helper to perform BFS and highlight connected elements
    function highlightNetwork(startNodeId) {
        // Clear previous state
        entities.forEach(ent => ent.classList.remove('highlighted', 'active-root'));
        links.forEach(lnk => lnk.classList.remove('highlighted'));

        const visitedNodes = new Set();
        const visitedLinks = new Set();
        const queue = [startNodeId];
        visitedNodes.add(startNodeId);

        while (queue.length > 0) {
            const current = queue.shift();
            const connections = adj[current] || [];
            for (const conn of connections) {
                if (!visitedLinks.has(conn.linkElement)) {
                    visitedLinks.add(conn.linkElement);
                }
                if (!visitedNodes.has(conn.neighborId)) {
                    visitedNodes.add(conn.neighborId);
                    queue.push(conn.neighborId);
                }
            }
        }

        // Apply highlighted class
        visitedNodes.forEach(nodeId => {
            const nodeEl = document.getElementById(nodeId);
            if (nodeEl) {
                nodeEl.classList.add('highlighted');
            }
        });

        visitedLinks.forEach(linkEl => {
            linkEl.classList.add('highlighted');
        });

        // Mark the active root
        const rootEl = document.getElementById(startNodeId);
        if (rootEl) {
            rootEl.classList.add('active-root');
        }

        svg.classList.add('has-selection');
        activeRootId = startNodeId;
    }

    // Add click listeners
    svg.addEventListener('click', (event) => {
        let entityEl = event.target.closest('.entity');
        if(!entityEl) {
            entityEl = event.target.closest('.cluster');
        }

        if (entityEl) {
            // We clicked on an entity
            event.stopPropagation(); // prevent document-level click handler
            const clickedId = entityEl.id;

            if (activeRootId === clickedId) {
                // Clicking the already active root deselects it
                clearSelection();
            } else {
                // Otherwise highlight its network
                highlightNetwork(clickedId);
            }
        } else {
            // Clicking on the SVG background (or other non-entity element) clears selection
            clearSelection();
        }
    });

    // Also support clicking outside the SVG to clear selection
    document.addEventListener('click', (event) => {
        if (!svg.contains(event.target)) {
            clearSelection();
        }
    });
});