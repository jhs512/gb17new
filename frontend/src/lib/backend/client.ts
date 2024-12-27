import { paths } from "@/lib/backend/apiV1/schema";
import createClient from "openapi-fetch";

const client = createClient<paths>({
  baseUrl: process.env.NEXT_PUBLIC_API_BASE_URL,
  credentials: "include",
});

export default client;
