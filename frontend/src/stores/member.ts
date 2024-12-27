"use client";

import { components } from "@/lib/backend/apiV1/schema";
import React from "react";
import { useState } from "react";

type Member = components["schemas"]["MemberDto"];

export const MemberContext = React.createContext<{
  loginMember: Member;
  setLoginMember: (member: Member) => void;
  removeLoginMember: () => void;
  isLogin: boolean;
  isLoginMemberPending: boolean;
}>({
  loginMember: createEmptyMember(),
  setLoginMember: () => {},
  removeLoginMember: () => {},
  isLogin: false,
  isLoginMemberPending: true,
});

function createEmptyMember(): Member {
  return {
    id: 0,
    createDate: "",
    modifyDate: "",
    name: "",
    nickname: "",
    profileImgUrl: "",
    authorities: [],
    social: false,
  };
}

export function useLoginMember() {
  const [isLoginMemberPending, setLoginMemberPending] = useState(true);
  const [loginMember, _setLoginMember] = useState<Member>(createEmptyMember());

  const removeLoginMember = () => {
    _setLoginMember(createEmptyMember());
    setLoginMemberPending(false);
  };

  const isLogin = loginMember.id !== 0;

  return {
    loginMember,
    removeLoginMember,
    isLogin,
    isLoginMemberPending,
    setLoginMember: (member: Member) => {
      _setLoginMember(member);
      setLoginMemberPending(false);
    },
  };
}
