window.onload = function () {
  window.ui = SwaggerUIBundle({
    url: "./openapi.json",
    dom_id: "#swagger-ui",
    deepLinking: true,
    filter: true,
    validatorUrl: null,
    presets: [SwaggerUIBundle.presets.apis, SwaggerUIStandalonePreset],
    plugins: [SwaggerUIBundle.plugins.DownloadUrl],
    layout: "StandaloneLayout"
  });
};
