package ru.netology.pages;

import com.codeborne.selenide.ElementsCollection;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    private ElementsCollection cards = $$(".list__item div");
    private String balanceStart = "баланс: ";
    private String balanceFinish = " р.";
    
    public DashboardPage() {
    }
    
    public MoneyTransferPage transferMoneyTo(int id) {
        cards.findBy(attribute("data-test-id", DataHelper.getCardTestId(id)))
                .find(".button").click();
        return new MoneyTransferPage();
    }
    
    public int getCardBalance(int id) {
        String text = cards.findBy(attribute("data-test-id", DataHelper.getCardTestId(id))).text();
        return extractBalance(text);
    }
    
    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
    
    public void headingShouldBeVisible(){
        $x("//*[text() = 'Ваши карты']").should(visible);
    }
}
