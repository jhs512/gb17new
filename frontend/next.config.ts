import type { NextConfig } from "next";

declare global {
  namespace NodeJS {
    interface ProcessEnv {
      NEXT_PUBLIC_API_BASE_URL: string;
    }
  }
}

const nextConfig: NextConfig = {
  /* config options here */
};

export default nextConfig;
