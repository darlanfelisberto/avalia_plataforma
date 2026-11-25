(() => {
    'use strict';
    const sidebar = document.getElementById('menuLateral');
    const toggle = document.querySelector('.menu-toggle');
    if (!sidebar || !toggle) return;
    sidebar.addEventListener('shown.bs.offcanvas', () => toggle.setAttribute('aria-expanded', 'true'));
    sidebar.addEventListener('hidden.bs.offcanvas', () => toggle.setAttribute('aria-expanded', 'false'));
})();
