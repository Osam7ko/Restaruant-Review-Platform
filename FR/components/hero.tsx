"use client";

import { useLanguage } from "@/providers/language-provider";

export default function Hero() {
  const { t } = useLanguage();

  return (
    <div className="relative h-[400px] mb-8">
      <div className="absolute inset-0">
        <img
          src="/hero-image.jpg?height=400&width=1920"
          alt="Restaurant interior"
          className="w-full h-full object-cover"
        />
        <div className="absolute inset-0 bg-black/50" />
      </div>
      <div className="relative max-w-[1200px] mx-auto h-full flex flex-col justify-center px-4">
        <h1 className="text-4xl md:text-5xl font-bold text-white mb-4">
          {t('hero.title')}
        </h1>
        <p className="text-lg md:text-xl text-white/90 max-w-[600px]">
          {t('hero.subtitle')}
        </p>
      </div>
    </div>
  );
}
