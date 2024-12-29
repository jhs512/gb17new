import type { Metadata } from "next";
import ClientLayout from "./ClientLayout";
import "./globals.css";
import { Toaster } from "@/components/ui/toaster";

import localFont from "next/font/local";

const pretendard = localFont({
  src: "./../../node_modules/pretendard/dist/web/variable/woff2/PretendardVariable.woff2",
  display: "swap",
  weight: "45 920",
  variable: "--font-pretendard",
});

export const metadata: Metadata = {
  title: "슬로그",
  description: "개발자 위키/블로그",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko" className={`${pretendard.variable}`}>
      <body className={`${pretendard.className} flex flex-col min-h-[100dvh]`}>
        <ClientLayout>
          {children}
          <Toaster />
        </ClientLayout>
      </body>
    </html>
  );
}
