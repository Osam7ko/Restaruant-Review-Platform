/**
 * Get the API base URL from environment variables
 */
export function getApiBaseUrl(): string {
  const apiUrl = process.env.NEXT_PUBLIC_API_URL;
  if (!apiUrl) {
    throw new Error('NEXT_PUBLIC_API_URL environment variable is not defined');
  }
  return apiUrl;
}

/**
 * Get the full URL for a photo by its filename
 */
export function getPhotoUrl(photoFilename: string): string {
  const baseUrl = getApiBaseUrl();
  return `${baseUrl}/photos/${photoFilename}`;
}

/**
 * Get the full API URL for any endpoint
 */
export function getApiUrl(endpoint: string): string {
  const baseUrl = getApiBaseUrl();
  return `${baseUrl}${endpoint.startsWith('/') ? endpoint : `/${endpoint}`}`;
}
