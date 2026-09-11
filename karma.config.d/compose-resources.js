// Karma (via karma-webpack) only serves files that webpack's module graph actually
// touches, so it never picks up composeResources: those are fetched at runtime with a
// plain fetch('/composeResources/<package>/...') call that webpack never sees. Without
// this, that fetch 404s and corrupts the Skia canvas render for any UI that loads a
// drawable/string resource (e.g. navigation bar icons), crashing the whole test.
config.set({
  files: config.files.concat([
    {
      pattern: "kotlin/composeResources/**/*",
      included: false,
      served: true,
      watched: false,
    },
  ]),
  proxies: Object.assign({}, config.proxies, {
    "/composeResources/": "/base/kotlin/composeResources/",
  }),
});

// The default Mocha per-test timeout (2s) is too short for tests that exercise real
// async I/O (e.g. DataStore-backed state) instead of purely virtual test-clock time.
config.set({
  client: Object.assign({}, config.client, { mocha: { timeout: 10000 } }),
});
