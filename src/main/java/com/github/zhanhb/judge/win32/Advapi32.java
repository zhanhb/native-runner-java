package com.github.zhanhb.judge.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinNT.PSID;
import com.sun.jna.platform.win32.WinNT.PSIDByReference;
import com.sun.jna.win32.W32APIOptions;
import java.util.Arrays;
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
            PSIDByReference pSid);

    @SuppressWarnings({"PublicField", "PublicInnerClass"})
    class SID_IDENTIFIER_AUTHORITY extends Structure {

        public byte[] Value = new byte[6]; // the length of the value must be 6

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
     * @param tokenHandle
     * @param tokenInformationClass TOKEN_INFORMATION_CLASS
     * @param tokenInformation
     * @param tokenInformationLength
     * @return
     * @see WinNT.TOKEN_INFORMATION_CLASS
     * @see
     * https://msdn.microsoft.com/en-us/library/windows/desktop/aa379591(v=vs.85).aspx
     */
    boolean SetTokenInformation(
            HANDLE tokenHandle,
            int /*TOKEN_INFORMATION_CLASS*/ tokenInformationClass,
            Structure tokenInformation,
            int /*DWORD*/ tokenInformationLength
    );

    @SuppressWarnings({"PublicField", "PublicInnerClass"})
    class TOKEN_MANDATORY_LABEL extends Structure {

        public SID_AND_ATTRIBUTES Label;

        @Override
        protected List<String> getFieldOrder() {
            return Collections.singletonList("Label");
        }

    }

    @SuppressWarnings({"PublicField", "PublicInnerClass"})
    class SID_AND_ATTRIBUTES extends Structure {

        /**
         * Pointer to a SID structure.
         */
        public Pointer Sid;

        /**
         * Specifies attributes of the SID. This value contains up to 32 one-bit
         * flags. Its meaning depends on the definition and use of the SID.
         */
        public int Attributes;

        public SID_AND_ATTRIBUTES() {
            super();
        }

        public SID_AND_ATTRIBUTES(Pointer memory) {
            super(memory);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("Sid", "Attributes");
        }

    }

    /*https://msdn.microsoft.com/en-us/library/windows/desktop/aa446631(v=vs.85).aspx*/
    Pointer FreeSid(PSID pSid);

    int SECURITY_MANDATORY_LOW_RID = 0x1000;
    int SE_GROUP_INTEGRITY = 0x00000020;

    int SANDBOX_INERT = 2;

}
