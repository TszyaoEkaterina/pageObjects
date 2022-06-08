package ru.netology.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.pages.DashboardPage;
import ru.netology.data.DataHelper;
import ru.netology.pages.LoginPage;
import ru.netology.pages.VerificationPage;

import static com.codeborne.selenide.Condition.ownText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    DashboardPage dashboardPage = new DashboardPage();
    int cardOneBalance = dashboardPage.getCardBalance(1);
    int cardTwoBalance = dashboardPage.getCardBalance(2);
    
    @BeforeAll
    static void validLoginAndOpenDashboardPage() {
        open("http://localhost:9999");
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = LoginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }
    
    @Test
    void shouldTransferFromFirstCardToSecond() {
        int amount = 2000;
        var moneyTransferPage = dashboardPage.transferMoneyTo(2);
        moneyTransferPage.transferMoneyFrom(1, amount);
        dashboardPage.headingShouldBeVisible();
        assertEquals(cardOneBalance - amount, dashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance + amount, dashboardPage.getCardBalance(2));
    }
    
    @Test
    void shouldTransferFromSecondCardToFirst() {
        int amount = 2000;
        var moneyTransferPage = dashboardPage.transferMoneyTo(1);
        moneyTransferPage.transferMoneyFrom(2, amount);
        dashboardPage.headingShouldBeVisible();
        assertEquals(cardOneBalance + amount, dashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance - amount, dashboardPage.getCardBalance(2));
    }
    
    @Test
    void buttonCancelShouldWork() {
        var moneyTransferPage = dashboardPage.transferMoneyTo(1);
        moneyTransferPage.cancel();
        assertEquals(cardOneBalance, dashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance, dashboardPage.getCardBalance(2));
    }
    
    @Test
    void transferFromAndToTheSameCard() {
        int amount = 2000;
        var moneyTransferPage = dashboardPage.transferMoneyTo(2);
        moneyTransferPage.transferMoneyFrom(2, amount);
        //there should not be any changes in balance
        assertEquals(cardOneBalance, dashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance, dashboardPage.getCardBalance(2));
    }
    
    @Test
    void shouldNotTransferFromWrongCard() {
        int amount = 2000;
        var moneyTransferPage = dashboardPage.transferMoneyTo(2);
        moneyTransferPage.transferMoneyFrom(5, amount);
        moneyTransferPage.shouldPrintError();
    }
    
    @Test
    void shouldNotTransferAmountAboveBalance() {
        int amount = 15000;
        var moneyTransferPage = dashboardPage.transferMoneyTo(1);
        moneyTransferPage.transferMoneyFrom(2, amount);
        moneyTransferPage.shouldPrintError();
        assertEquals(cardOneBalance, dashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance, dashboardPage.getCardBalance(2));
    }
}
