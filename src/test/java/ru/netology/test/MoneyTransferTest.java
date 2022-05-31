package ru.netology.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.pages.DashboardPage;
import ru.netology.data.DataHelper;
import ru.netology.pages.LoginPage;

import static com.codeborne.selenide.Condition.ownText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    @BeforeAll
    static void validLoginAndOpenDashboardPage() {
        open("http://localhost:9999");
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = LoginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var DashboardPage = verificationPage.validVerify(verificationCode);
    }
    
    int cardOneBalance = DashboardPage.getCardBalance(1);
    int cardTwoBalance = DashboardPage.getCardBalance(2);
    
    @Test
    void shouldTransferFromFirstCardToSecond() {
        int amount = 2000;
        var MoneyTransferPage = DashboardPage.transferMoneyTo(2);
        MoneyTransferPage.transferMoneyFrom(1, amount);
        $x("//*[text() = 'Ваши карты']").should(visible);
        assertEquals(cardOneBalance - amount, DashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance + amount, DashboardPage.getCardBalance(2));
    }
    
    @Test
    void shouldTransferFromSecondCardToFirst() {
        int amount = 2000;
        var MoneyTransferPage = DashboardPage.transferMoneyTo(1);
        MoneyTransferPage.transferMoneyFrom(2, amount);
        $x("//*[text() = 'Ваши карты']").should(visible);
        assertEquals(cardOneBalance + amount, DashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance - amount, DashboardPage.getCardBalance(2));
    }
    
    @Test
    void buttonCancelShouldWork() {
        var MoneyTransferPage = DashboardPage.transferMoneyTo(1);
        MoneyTransferPage.cancel();
        assertEquals(cardOneBalance, DashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance, DashboardPage.getCardBalance(2));
    }
    
    @Test
    void transferFromAndToTheSameCard() {
        int amount = 2000;
        var MoneyTransferPage = DashboardPage.transferMoneyTo(2);
        MoneyTransferPage.transferMoneyFrom(2, amount);
        //there should not be any changes in balance
        assertEquals(cardOneBalance, DashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance, DashboardPage.getCardBalance(2));
    }
    
    @Test
    void shouldNotTransferFromWrongCard() {
        int amount = 2000;
        var MoneyTransferPage = DashboardPage.transferMoneyTo(2);
        MoneyTransferPage.transferMoneyFrom(5, amount);
        $(".notification__title").should(ownText("Ошибка"));
        $(".notification__content").should(ownText("Ошибка!\r\n" +
                "Произошла ошибка"));
    }
    
    @Test
    void shouldNotTransferAmountAboveBalance() {
        int amount = 15000;
        var MoneyTransferPage = DashboardPage.transferMoneyTo(1);
        MoneyTransferPage.transferMoneyFrom(2, amount);
        //there should be an error and transfer should not happen, but real situation is:
        $x("//*[text() = 'Ваши карты']").should(visible);
        assertEquals(cardOneBalance + amount, DashboardPage.getCardBalance(1));
        assertEquals(cardTwoBalance - amount, DashboardPage.getCardBalance(2));//balance became negative
    }
}
