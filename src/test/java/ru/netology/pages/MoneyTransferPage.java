package ru.netology.pages;

import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MoneyTransferPage {
    public static void transferMoneyFrom(int id, int amount) {
        $("[data-test-id=amount] input").sendKeys(Keys.CONTROL + "A", Keys.BACK_SPACE);
        $("[data-test-id=amount] input").setValue(String.valueOf(amount));
        $("[data-test-id=from] input").sendKeys(Keys.CONTROL + "A", Keys.BACK_SPACE);
        $("[data-test-id=from] input").setValue(DataHelper.getCardNumber(id));
        $("[data-test-id =action-transfer]").click();
    }
    
    public static void cancel() {
        $(byText("Отмена")).click();
        $x("//*[text() = 'Ваши карты']").should(visible);
    }
}