import arTranslations from '@/locales/ar.json';
import enTranslations from '@/locales/en.json';

export type Language = 'ar' | 'en';

export const translations = {
  ar: arTranslations,
  en: enTranslations,
};

export const defaultLanguage: Language = 'ar';

export const languages = [
  { code: 'ar' as Language, name: 'العربية', dir: 'rtl' },
  { code: 'en' as Language, name: 'English', dir: 'ltr' },
];

export function getTranslation(language: Language, key: string): string {
  const keys = key.split('.');
  let value: any = translations[language];
  
  for (const k of keys) {
    if (value && typeof value === 'object' && k in value) {
      value = value[k];
    } else {
      // Fallback to English if key not found in current language
      value = translations.en;
      for (const fallbackKey of keys) {
        if (value && typeof value === 'object' && fallbackKey in value) {
          value = value[fallbackKey];
        } else {
          return key; // Return key if not found in any language
        }
      }
      break;
    }
  }
  
  return typeof value === 'string' ? value : key;
}

export function getLanguageDirection(language: Language): 'ltr' | 'rtl' {
  return language === 'ar' ? 'rtl' : 'ltr';
}

export function getLanguageName(language: Language): string {
  const lang = languages.find(l => l.code === language);
  return lang ? lang.name : language;
}
