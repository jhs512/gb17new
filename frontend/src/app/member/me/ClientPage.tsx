"use client";

import { MemberContext } from "@/stores/member";
import { use } from "react";

export default function ClientPage() {
  const { isLogin, loginMember } = use(MemberContext);

  if (!isLogin) return <div>로그인 후 이용해주세요.</div>;

  return (
    <div className="flex-1 flex items-center justify-center">
      <div>
        <div className="text-center">{loginMember.name}</div>
        <img
          className="rounded-full mt-2"
          src={loginMember.profileImgUrl}
          width={100}
        />
      </div>
    </div>
  );
}
