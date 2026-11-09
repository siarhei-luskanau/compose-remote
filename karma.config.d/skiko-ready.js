// Workaround: Compose 1.13.0-beta02 SkikoComposeUiTest touches Skia in its constructor before awaiting skiko wasm.
;(function (config) {
    const fs = require('fs');
    const path = require('path');
    const kotlinDir = path.resolve(config.basePath, 'kotlin');
    if (!fs.existsSync(path.resolve(kotlinDir, 'skiko.mjs'))) return;

    const setupFile = path.resolve(kotlinDir, 'await-skiko-setup.mjs');
    fs.writeFileSync(
        setupFile,
        'import { awaitSkiko } from "./skiko.mjs";\n' +
        'before(function () { this.timeout(60000); return awaitSkiko; });\n',
    );
    config.files.unshift(setupFile);
    config.preprocessors[setupFile] = ['webpack'];
})(config);
