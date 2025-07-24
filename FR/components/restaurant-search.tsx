"use client";

import { useState, useRef } from "react";
import { Search, Star } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { RestaurantSearchParams } from "@/domain/domain";
import { useLanguage } from "@/providers/language-provider";

interface RestaurantSearchProps {
  searchRestaurants: (params: RestaurantSearchParams) => Promise<void>;
}

export default function RestaurantSearch(props: RestaurantSearchProps) {
  const { searchRestaurants } = props;
  const { t } = useLanguage();

  const [query, setQuery] = useState("");
  const [minRating, setMinRating] = useState<number | undefined>(undefined);

  // Use a ref to track if this is the initial render
  const isInitialMount = useRef(true);

  const handleSearch = async () => {
    await searchRestaurants({
      q: query,
      minRating,
    });
  };

  const handleSearchFormSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await handleSearch();
  };

  const handleMinRatingFilter = async (value: number) => {
    // First update the state
    if (minRating !== value) {
      // Selecting a new rating
      setMinRating(value);

      // Wait until next tick to ensure state is updated
      setTimeout(() => {
        searchRestaurants({
          q: query,
          minRating: value, // Use the new value directly
        });
      }, 0);
    } else {
      // Deselecting the current rating
      setMinRating(undefined);

      // Wait until next tick to ensure state is updated
      setTimeout(() => {
        searchRestaurants({
          q: query,
          minRating: undefined, // Use undefined directly
        });
      }, 0);
    }
  };

  return (
    <div>
      <form onSubmit={handleSearchFormSubmit} className="flex gap-2 mb-4">
        <Input
          type="text"
          placeholder={t('search.placeholder')}
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="flex-grow"
        />
        <Button type="submit">
          <Search className="mr-2 h-4 w-4" /> {t('search.button')}
        </Button>
      </form>
      <div className="flex mb-8 gap-2">
        <Button
          onClick={() => handleMinRatingFilter(2)}
          variant={minRating === 2 ? "default" : "outline"}
        >
          <Star /> {t('rating.filters.2plus')}
        </Button>

        <Button
          onClick={() => handleMinRatingFilter(3)}
          variant={minRating === 3 ? "default" : "outline"}
        >
          <Star /> {t('rating.filters.3plus')}
        </Button>

        <Button
          onClick={() => handleMinRatingFilter(4)}
          variant={minRating === 4 ? "default" : "outline"}
        >
          <Star /> {t('rating.filters.4plus')}
        </Button>
      </div>
    </div>
  );
}
