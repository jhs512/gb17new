import { redirect } from "next/navigation";

export default function Page({
  params,
  searchParams,
}: {
  params: { id: string };
  searchParams: { [key: string]: string | undefined };
}) {
  const { id } = params;
  const hash = searchParams["#"];

  const redirectPath = `/p/${id}${hash ? `#${hash}` : ""}`;
  redirect(redirectPath);
}
