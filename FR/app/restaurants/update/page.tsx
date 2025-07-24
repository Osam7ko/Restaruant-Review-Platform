import { Suspense } from "react";
import UpdateRestaurantPageInner from "./UpdateRestaurantPageInner";

export const dynamic = "force-dynamic";
export const fetchCache = "force-no-store";

export default function Page() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <UpdateRestaurantPageInner />
    </Suspense>
  );
}
