import type { NextConfig } from "next";

declare global {
  namespace NodeJS {
    interface ProcessEnv {
      NEXT_PUBLIC_API_BASE_URL: string;
      NEXT_PUBLIC_FRONTEND_BASE_URL: string;
    }
  }
}

const nextConfig: NextConfig = {
  transpilePackages: [
    "@toast-ui/editor",
    "@toast-ui/react-editor",
    "tui-color-picker",
  ],
  eslint: {
    ignoreDuringBuilds: true,
  },
  typescript: {
    ignoreBuildErrors: true,
  },
};

export default nextConfig;
