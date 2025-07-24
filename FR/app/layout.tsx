"use client";

import AuthButton from "@/components/auth-button";
import LanguageSwitcher from "@/components/language-switcher";
import { AppAuthProvider } from "@/providers/app-auth-provider";
import { AppContextProvider } from "@/providers/app-context-provider";
import { LanguageProvider, useLanguage } from "@/providers/language-provider";
import "@/styles/globals.css";
import { Inter } from "next/font/google";
import Link from "next/link";
import type React from "react"; // Import React

const inter = Inter({ subsets: ["latin"] });

function LayoutContent({ children }: { children: React.ReactNode }) {
  const { t, language, dir } = useLanguage();

  return (
    <html lang={language} dir={dir}>
      <head>
        <title>Restaurant Review Platform</title>
        <meta name="description" content="Discover and review local restaurants" />
      </head>
      <body className={inter.className}>
        <header className="border-b">
          <div className="max-w-[1200px] mx-auto px-4 py-4 flex justify-between items-center">
            <Link href="/" className="text-2xl font-bold">
              {t('header.title')}
            </Link>
            <div className="flex items-center gap-4">
              <LanguageSwitcher />
              <AuthButton />
            </div>
          </div>
        </header>
        {children}
      </body>
    </html>
  );
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <AppAuthProvider>
      <AppContextProvider>
        <LanguageProvider>
          <LayoutContent>{children}</LayoutContent>
        </LanguageProvider>
      </AppContextProvider>
    </AppAuthProvider>
  );
}
