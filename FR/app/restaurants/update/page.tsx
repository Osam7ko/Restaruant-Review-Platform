"use client";

import { useForm, FormProvider } from "react-hook-form";
import { Card, CardContent } from "@/components/ui/card";
import { useAppContext } from "@/providers/app-context-provider";
import { useLanguage } from "@/providers/language-provider";
import { CreateRestaurantRequest, Photo } from "@/domain/domain";
import CreateRestaurantForm from "@/components/create-restaurant-form";
import { useEffect, useState } from "react";
import axios from "axios";
import { useRouter, useSearchParams } from "next/navigation";

type FormData = {
  name: string;
  cuisineType: string;
  contactInformation: string;
  address: {
    streetNumber: string;
    streetName: string;
    unit?: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
  };
  operatingHours: {
    monday: { openTime: string; closeTime: string } | undefined;
    tuesday: { openTime: string; closeTime: string } | undefined;
    wednesday: { openTime: string; closeTime: string } | undefined;
    thursday: { openTime: string; closeTime: string } | undefined;
    friday: { openTime: string; closeTime: string } | undefined;
    saturday: { openTime: string; closeTime: string } | undefined;
    sunday: { openTime: string; closeTime: string } | undefined;
  };
  photos: string[];
};

export default function UpdateRestaurantPage() {
  const { apiService } = useAppContext();
  const { t } = useLanguage();
  const [error, setError] = useState<string | undefined>();
  const [loading, setLoading] = useState<boolean>(true);
  const searchParams = useSearchParams();

  const restaurantId = searchParams.get("id");
  const router = useRouter();

  const methods = useForm<FormData>({
    defaultValues: {
      name: "",
      cuisineType: "",
      contactInformation: "",
      address: {
        streetNumber: "",
        streetName: "",
        unit: "",
        city: "",
        state: "",
        postalCode: "",
        country: "",
      },
      operatingHours: {
        monday: undefined,
        tuesday: undefined,
        wednesday: undefined,
        thursday: undefined,
        friday: undefined,
        saturday: undefined,
        sunday: undefined,
      },
      photos: [],
    },
  });

  useEffect(() => {
    const doUseEffect = async () => {
      if (!apiService) {
        return;
      }

      setLoading(true);
      setError(undefined);

      if (!restaurantId) {
        setError(t('errors.restaurantNotFound'));
        setLoading(false);
        return;
      }

      try {
        const restaurant = await apiService.getRestaurant(restaurantId);

        // Update form with restaurant data
        methods.reset({
          name: restaurant.name,
          cuisineType: restaurant.cuisineType,
          contactInformation: restaurant.contactInformation,
          address: {
            streetNumber: restaurant.address.streetNumber,
            streetName: restaurant.address.streetName,
            unit: restaurant.address.unit || "",
            city: restaurant.address.city,
            state: restaurant.address.state,
            postalCode: restaurant.address.postalCode,
            country: restaurant.address.country,
          },
          operatingHours: {
            monday: restaurant.operatingHours.monday,
            tuesday: restaurant.operatingHours.tuesday,
            wednesday: restaurant.operatingHours.wednesday,
            thursday: restaurant.operatingHours.thursday,
            friday: restaurant.operatingHours.friday,
            saturday: restaurant.operatingHours.saturday,
            sunday: restaurant.operatingHours.sunday,
          },
          photos: restaurant.photos?.map((photo) => photo.url) || [],
        });
      } catch (err) {
        if (axios.isAxiosError(err)) {
          if (err.response?.status === 404) {
            setError(t('errors.restaurantNotFound'));
          } else {
            setError(
              `${t('errors.fetchError')}: ${err.response?.status}: ${err.response?.data}`,
            );
          }
        } else {
          setError(t('errors.fetchError'));
        }
      } finally {
        setLoading(false);
      }
    };

    doUseEffect();
  }, [apiService, restaurantId, methods, t]);

  const uploadPhoto = async (file: File, caption?: string): Promise<Photo> => {
    if (null == apiService) {
      throw Error(t('errors.apiNotAvailable'));
    }
    return apiService.uploadPhoto(file, caption);
  };

  const onSubmit = async (data: FormData) => {
    console.log("Form submitted:", data);

    try {
      const updateRestaurantRequest: CreateRestaurantRequest = {
        name: data.name,
        cuisineType: data.cuisineType,
        contactInformation: data.contactInformation,
        address: data.address,
        operatingHours: data.operatingHours,
        photoIds: data.photos,
      };

      if (null == apiService) {
        throw Error(t('errors.apiNotAvailable'));
      }

      setError(undefined);

      if (restaurantId) {
        // Update existing restaurant
        await apiService.updateRestaurant(
          restaurantId,
          updateRestaurantRequest,
        );
        router.push("/");
      } else {
        // Create new restaurant (fallback, though this shouldn't happen with your validation)
        await apiService.createRestaurant(updateRestaurantRequest);
        router.push("/");
      }
    } catch (err) {
      if (axios.isAxiosError(err)) {
        // This confirms it's an Axios error
        if (err.response?.status === 400) {
          // Extract the JSON error body
          const errorData = err.response.data?.message;
          setError(errorData);
        } else {
          // Handle other status codes
          setError(`API Error: ${err.response?.status}: ${err.response?.data}`);
        }
      } else {
        // Handle non-Axios errors
        setError(String(err));
      }
    }
  };

  if (loading) {
    return (
      <div className="min-h-[100vh] h-full flex items-center justify-center">
        <p>{t('loading.general')}</p>
      </div>
    );
  }

  return (
    <div className="max-w-[800px] mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-6">{t('restaurantForm.update.title')}</h1>
      <Card>
        <CardContent className="pt-6">
          <FormProvider {...methods}>
            <form onSubmit={methods.handleSubmit(onSubmit)}>
              <CreateRestaurantForm uploadPhoto={uploadPhoto} error={error} isUpdate={true} />
            </form>
          </FormProvider>
        </CardContent>
      </Card>
    </div>
  );
}
