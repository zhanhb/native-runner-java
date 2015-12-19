package com.github.zhanhb.judge.jna;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinNT.SID_AND_ATTRIBUTES;
import com.sun.jna.win32.W32APIOptions;
import java.util.Collections;
import java.util.List;

public interface Advapi32 extends com.sun.jna.platform.win32.Advapi32 {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    Advapi32 INSTANCE = (Advapi32) Native.loadLibrary("Advapi32", Advapi32.class, W32APIOptions.UNICODE_OPTIONS);

    boolean CreateRestrictedToken(
            WinNT.HANDLE ExistingTokenHandle,
            int /*DWORD*/ Flags,
            int /*DWORD*/ DisableSidCount,
            SID_AND_ATTRIBUTES[] SidsToDisable,
            int /*DWORD*/ DeletePrivilegeCount,
            WinNT.LUID_AND_ATTRIBUTES[] PrivilegesToDelete,
            int /*DWORD*/ RestrictedSidCount,
            SID_AND_ATTRIBUTES[] SidsToRestrict,
            WinNT.HANDLEByReference NewTokenHandle
    );

    boolean AllocateAndInitializeSid(
            SID_IDENTIFIER_AUTHORITY pIdentifierAuthority,
            byte /*BYTE*/ nSubAuthorityCount,
            int /*DWORD*/ dwSubAuthority0,
            int /*DWORD*/ dwSubAuthority1,
            int /*DWORD*/ dwSubAuthority2,
            int /*DWORD*/ dwSubAuthority3,
            int /*DWORD*/ dwSubAuthority4,
            int /*DWORD*/ dwSubAuthority5,
            int /*DWORD*/ dwSubAuthority6,
            int /*DWORD*/ dwSubAuthority7,
            WinNT.PSIDByReference pSid);

    @SuppressWarnings({"PublicField", "PublicInnerClass"})
    class SID_IDENTIFIER_AUTHORITY extends Structure {

        public byte[] Value = new byte[6];

        public SID_IDENTIFIER_AUTHORITY() {
        }

        public SID_IDENTIFIER_AUTHORITY(byte... values) {
            if (values.length != 6) {
                throw new IllegalArgumentException();
            }
            this.Value = values;
        }

        @Override
        protected List<String> getFieldOrder() {
            return Collections.singletonList("Value");
        }

    }

    /**
     * @param TokenHandle
     * @param TokenInformationClass TOKEN_INFORMATION_CLASS
     * @param TokenInformation
     * @param TokenInformationLength
     * @return
     * @see WinNT.TOKEN_INFORMATION_CLASS
     * @see
     * https://msdn.microsoft.com/en-us/library/windows/desktop/aa379591(v=vs.85).aspx
     */
    boolean SetTokenInformation(
            HANDLE TokenHandle,
            int /*TOKEN_INFORMATION_CLASS*/ TokenInformationClass,
            Pointer TokenInformation,
            int /*DWORD*/ TokenInformationLength
    );

}