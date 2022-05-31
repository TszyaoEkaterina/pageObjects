package ru.netology.pages;

import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    public DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
        $("[data-test-id=code] input").setValue(verificationCode.getCode());
        $("[data-test-id=action-verify]").click();
        return new DashboardPage();
    }
}
