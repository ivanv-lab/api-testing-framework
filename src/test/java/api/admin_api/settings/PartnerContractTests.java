package api.admin_api.settings;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.git.ivanv_lab.framework.fabric.admin_api.settings.PartnerFabric;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.partners.PartnerPost;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.partners.PartnerTransport;

import static org.junit.jupiter.api.Assertions.*;

@Epic("admin-console-api")
@Feature("Настройки")
@Story("Клиенты")
@Tag("acapi-settings-partners")
public class PartnerContractTests {

    private final PartnerFabric partnerFabric = new PartnerFabric();

    @DisplayName("Контракт: пустой список клиентов")
    @Test
    void contractEmptyListOfPartners() {
        assertNull(partnerFabric.getAllPartners(), "Ожидалось, что массив клиентов будет пуст");
    }

    @DisplayName("Контракт: заполненный список клиентов")
    @Test
    void contractFullListOfPartners() {
        for(PartnerPost partnerPost: partnersToCreate())
            partnerFabric.createPartner(partnerPost);

        assertNotNull(partnerFabric.getAllPartners());
        assertEquals(4, partnerFabric.getAllPartners().size());

        for(PartnerPost partnerPost: partnersToCreate())
            partnerFabric.deletePartnerByName(partnerPost.getName());
    }

    @DisplayName("Контракт: фильтрация по id")
    @Test
    void contractFilterById() {

    }

    @DisplayName("Контракт: фильтрация по name")
    @Test
    void contractFilterByName() {

    }

    @DisplayName("Контракт: фильтрация по transports")
    @Test
    void contractFilterByTransports() {

    }

    @DisplayName("Контракт: фильтрация по status")
    @Test
    void contractFilterByStatus() {

    }

    @DisplayName("Контракт: фильтрация по prepaid")
    @Test
    void contractFilterByPrepaid() {

    }

    @DisplayName("Контракт: создание клиента с одним транспортом")
    @Test
    void contractCreateMinPartner() {

    }

    @DisplayName("Контракт: создание клиента со всеми транспортами и настройками")
    @Test
    void contractCreateFullPartner() {

    }

    @DisplayName("Контракт: редактирование клиента")
    @Test
    void contractUpdatePartner() {

    }


    // Отрицательные

    @DisplayName("Контракт: фильтрация по несуществующему параметру")
    @Test
    void contractFilterByUnexpectedParameter() {

    }

    @DisplayName("Контракт: фильтрация по несуществующему значению параметра")
    @Test
    void contractFilterByUnexpectedValueOfParameter() {

    }

    @DisplayName("Контракт: создание клиента без обязательного параметра")
    @Test
    void contractCreateWithoutRequiredParameter() {

    }

    @DisplayName("Контракт: создание клиента с некорректным значением параметра")
    @Test
    void contractCreateIncorrectValueOfParameter() {

    }

    private PartnerPost[] partnersToCreate(){
        return new PartnerPost[]{
                new PartnerPost.Builder()
                        .withName("CONTRACT_PARTNER_DATA_1")
                        .withPrepaid(true)
                        .withStatus(1)
                        .withTransports(new PartnerTransport[]{
                                new PartnerTransport.Builder().withTransport("SMS").build()
                        })
                        .build(),
                new PartnerPost.Builder()
                        .withName("CONTRACT_PARTNER_DATA_2")
                        .withPrepaid(true)
                        .withStatus(1)
                        .withTransports(new PartnerTransport[]{
                                new PartnerTransport.Builder().withTransport("SMS").build(),
                                new PartnerTransport.Builder().withTransport("Viber").build()
                        })
                        .build(),
                new PartnerPost.Builder()
                        .withName("CONTRACT_PARTNER_DATA_3")
                        .withPrepaid(true)
                        .withStatus(1)
                        .withTransports(new PartnerTransport[]{
                                new PartnerTransport.Builder().withTransport("Push").build()
                        })
                        .build(),
                new PartnerPost.Builder()
                        .withName("CONTRACT_PARTNER_DATA_4")
                        .withPrepaid(false)
                        .withStatus(0)
                        .withTransports(new PartnerTransport[]{
                                new PartnerTransport.Builder().withTransport("Call").build()
                        })
                        .build()
        };
    }
}
