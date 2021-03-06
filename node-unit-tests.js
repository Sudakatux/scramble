const fs = require('fs');
const jsdom = require ('jsdom');

const {JSDOM} = jsdom;

const { window } = new JSDOM(``, { runScripts: "dangerously" });

global.window = window;

function loadAndAppend (fname, w){
    var text =  fs.readFileSync(fname, { encoding: "utf-8" });
    var scriptEl = w.document.createElement("script");
    scriptEl.textContent = text;
    w.document.body.appendChild(scriptEl);
}

loadAndAppend("test.js",window);
