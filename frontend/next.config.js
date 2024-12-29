const nextConfig = {
  webpack: (config) => {
    config.resolve.alias = {
      ...config.resolve.alias,
      "monaco-editor": "monaco-editor/esm/vs/editor/editor.api",
    };
    return config;
  },
};

module.exports = nextConfig;
