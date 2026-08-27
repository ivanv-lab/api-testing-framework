package api.admin_api.settings;

import api.base.BaseApiTests;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import ru.git.ivanv_lab.framework.http.api.fabric.admin_api.settings.PartnerFabric;
import ru.git.ivanv_lab.framework.http.api.fabric.general.Transport;
import ru.git.ivanv_lab.framework.http.api.fabric.general.TransportFabric;
import ru.git.ivanv_lab.framework.json.contract_validator.core.JsonContractWorker;
import ru.git.ivanv_lab.framework.json.contract_validator.util.JsonMapperHolder;
import ru.git.ivanv_lab.framework.json.contracts.admin_api.partners.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Epic("admin-console-api")
@Feature("Настройки")
@Story("Клиенты")
@Tag("acapi-settings-partners")
public class PartnerContractTests extends BaseApiTests {

    private final PartnerFabric partnerFabric = new PartnerFabric(adminConsoleApiWorker.get());

    @DisplayName("Контракт: пустой список клиентов")
    @Test
    void contractEmptyListOfPartners() {
        assertEquals(List.of(), partnerFabric.getAll(), "Ожидалось, что массив клиентов будет пуст");
    }

    @DisplayName("Контракт: заполненный список клиентов")
    @Test
    void contractFullListOfPartners() {
        createPartners(partnersToCreate());
        try {
            assertNotNull(partnerFabric.getAll(), "Ожидалось, что массив клиентов не будет пуст");
            assertTrue(partnerFabric.getAll().size() >= 4);
        } finally {
            deletePartners(partnersToCreate());
        }
    }

    @DisplayName("Контракт: фильтрация по id")
    @Test
    void contractFilterById() {
        createPartners(partnersToCreate());
        int findingId = partnerFabric.createPartnerReturnId(new PartnerPost.Builder()
                .withName("CONTRACT_FILTER_BY_ID")
                .withPrepaid(true)
                .withTransports(new PartnerTransport[]{
                        new PartnerTransport.Builder().withTransport(Transport.SMS, transportFabric).build()
                })
                .withStatus(1)
                .build());

        PartnerListGet expected = new PartnerListGet(
                findingId,
                "CONTRACT_FILTER_BY_ID",
                new String[]{"SMS"},
                1,
                true
        );

        try {
            List<PartnerListGet> actuals = partnerFabric.getAllByFilter("?id=" + findingId);
            assertEquals(1, actuals.size());

            JsonContractWorker.assertContract(actuals.get(0), expected);
        } finally {
            deletePartners(partnersToCreate());
            partnerFabric.deleteById(findingId);
        }
    }

    @DisplayName("Контракт: фильтрация по name")
    @Test
    void contractFilterByName() {
        createPartners(partnersToCreate());
        int findingId = partnerFabric.createPartnerReturnId(new PartnerPost.Builder()
                .withName("CONTRACT_FILTER_BY_NAME")
                .withPrepaid(true)
                .withTransports(new PartnerTransport[]{
                        new PartnerTransport.Builder().withTransport(Transport.SMS, transportFabric).build()
                })
                .withStatus(1)
                .build());

        PartnerListGet expected = new PartnerListGet(
                findingId,
                "CONTRACT_FILTER_BY_NAME",
                new String[]{"SMS"},
                1,
                true
        );

        try {
            List<PartnerListGet> actuals = partnerFabric.getAllByFilter("?name=CONTRACT_FILTER_BY_NAME");
            assertEquals(1, actuals.size());

            JsonContractWorker.assertContract(actuals.get(0), expected);
        } finally {
            deletePartners(partnersToCreate());
            partnerFabric.deleteById(findingId);
        }
    }

    @DisplayName("Контракт: фильтрация по transports")
    @Test
    void contractFilterByTransports() {
        createPartners(partnersToCreate());

        int findingId = partnerFabric.createPartnerReturnId(new PartnerPost.Builder()
                .withName("CONTRACT_FILTER_BY_TRANSPORTS")
                .withPrepaid(true)
                .withTransports(new PartnerTransport[]{
                        new PartnerTransport.Builder().withTransport(Transport.EMAIL, transportFabric).build()
                })
                .withStatus(1)
                .build());

        PartnerListGet expected = new PartnerListGet(
                findingId,
                "CONTRACT_FILTER_BY_TRANSPORTS",
                new String[]{"Email"},
                1,
                true
        );

        try {
            List<PartnerListGet> actuals = partnerFabric.getAllByFilter("?transports=Email");
            assertEquals(1, actuals.size());

            JsonContractWorker.assertContract(actuals.get(0), expected);
        } finally {
            deletePartners(partnersToCreate());
            partnerFabric.deleteById(findingId);
        }
    }

    @DisplayName("Контракт: фильтрация по status")
    @Test
    void contractFilterByStatus() {
        createPartners(partnersToCreate());
        int findingId = partnerFabric.createPartnerReturnId(new PartnerPost.Builder()
                .withName("CONTRACT_FILTER_BY_STATUS")
                .withPrepaid(true)
                .withTransports(new PartnerTransport[]{
                        new PartnerTransport.Builder().withTransport(Transport.EMAIL, transportFabric).build()
                })
                .withStatus(0)
                .build());

        PartnerListGet expected = new PartnerListGet(
                findingId,
                "CONTRACT_FILTER_BY_STATUS",
                new String[]{"Email"},
                0,
                true
        );

        try {
            List<PartnerListGet> actuals = partnerFabric.getAllByFilter("?status=0");
            assertEquals(1, actuals.size());

            JsonContractWorker.assertContract(actuals.get(0), expected);
        } finally {
            deletePartners(partnersToCreate());
            partnerFabric.deleteById(findingId);
        }
    }

    @DisplayName("Контракт: фильтрация по prepaid")
    @Test
    void contractFilterByPrepaid() {
        createPartners(partnersToCreate());
        int findingId = partnerFabric.createPartnerReturnId(new PartnerPost.Builder()
                .withName("CONTRACT_FILTER_BY_PREPAID")
                .withPrepaid(false)
                .withTransports(new PartnerTransport[]{
                        new PartnerTransport.Builder().withTransport(Transport.EMAIL, transportFabric).build()
                })
                .withStatus(1)
                .build());

        PartnerListGet expected = new PartnerListGet(
                findingId,
                "CONTRACT_FILTER_BY_PREPAID",
                new String[]{"Email"},
                1,
                false
        );

        try {
            List<PartnerListGet> actuals = partnerFabric.getAllByFilter("?prepaid=false");
            assertEquals(1, actuals.size());

            JsonContractWorker.assertContract(actuals.get(0), expected);
        } finally {
            deletePartners(partnersToCreate());
            partnerFabric.deleteById(findingId);
        }
    }

    @DisplayName("Контракт: создание клиента с одним транспортом")
    @Test
    void contractCreateMinPartner() {
        PartnerPost partnerPost = new PartnerPost.Builder()
                .withName("CONTRACT_ONE_TRANSPORT")
                .withStatus(1)
                .withPrepaid(false)
                .withTransports(new PartnerTransport[]{
                        new PartnerTransport.Builder().withTransport(Transport.VIBER, transportFabric).build()
                })
                .build();
        int findingId = partnerFabric.createPartnerReturnId(partnerPost);

        PartnerGet expected = new PartnerGet(
                partnerPost.getName(),
                partnerPost.getTransports(),
                partnerPost.getStatus(),
                partnerPost.isPrepaid()
        );

        try {
            PartnerGet actual = partnerFabric.getById(findingId);
            JsonContractWorker.assertContract(actual, expected);
        } finally {
            partnerFabric.deleteById(findingId);
        }
    }

    @DisplayName("Контракт: создание клиента со всеми транспортами и настройками")
    @Test
    void contractCreateFullPartner() {
        PartnerPost partnerPost = new PartnerPost.Builder()
                .withName("CONTRACT_MANY_TRANSPORT")
                .withStatus(1)
                .withPrepaid(false)
                .withTransports(new PartnerTransport[]{
                        new PartnerTransport.Builder().withTransport(Transport.SMS, transportFabric)
                                .withMultisignature(true).withTemplateOnly(true)
                                .withModeration(true).build(),
                        new PartnerTransport.Builder().withTransport(Transport.CALL, transportFabric)
                                .withMultisignature(true).withTemplateOnly(true)
                                .withModeration(true).build(),
                        new PartnerTransport.Builder().withTransport(Transport.EMAIL, transportFabric)
                                .withMultisignature(true).withTemplateOnly(true)
                                .withModeration(true).build(),
                        new PartnerTransport.Builder().withTransport(Transport.PUSH, transportFabric)
                                .withTemplateOnly(true).withModeration(true).build(),
                        new PartnerTransport.Builder().withTransport(Transport.VIBER, transportFabric)
                                .withMultisignature(true).withTemplateOnly(true)
                                .withModeration(true).build(),
                        new PartnerTransport.Builder().withTransport(Transport.WHATSAPP, transportFabric)
                                .withMultisignature(true).withTemplateOnly(true)
                                .withModeration(true).build(),
                        new PartnerTransport.Builder().withTransport(Transport.CUSTOM, transportFabric)
                                .withMultisignature(true).withTemplateOnly(true)
                                .withModeration(true).build(),
                        new PartnerTransport.Builder().withTransport(Transport.MAIL_NOTIFY, transportFabric)
                                .withTemplateOnly(true)
                                .withModeration(true).build(),
                        new PartnerTransport.Builder().withTransport(Transport.TELEGRAM, transportFabric)
                                .withTemplateOnly(true)
                                .withModeration(true).build(),
                })
                .build();
        int findingId = partnerFabric.createPartnerReturnId(partnerPost);

        PartnerGet expected = new PartnerGet(
                partnerPost.getName(),
                partnerPost.getTransports(),
                partnerPost.getStatus(),
                partnerPost.isPrepaid()
        );

        try {
            PartnerGet actual = partnerFabric.getById(findingId);
            JsonContractWorker.assertContract(actual, expected);
        } finally {
            partnerFabric.deleteById(findingId);
        }
    }

    @DisplayName("Контракт: редактирование клиента")
    @Test
    void contractUpdatePartner() {
        PartnerPost partnerPost = new PartnerPost.Builder()
                .withName("CONTRACT_UPDATE")
                .withStatus(1)
                .withPrepaid(false)
                .withTransports(new PartnerTransport[]{
                        new PartnerTransport.Builder().withTransport(Transport.VIBER, transportFabric).build()
                })
                .build();
        int findingId = partnerFabric.createPartnerReturnId(partnerPost);

        PartnerPut putContract = new PartnerPut(
                "CONTRACT_UPDATED", new PartnerTransport[]{
                new PartnerTransport(1, true, true, true)
        }, true, 0);

        PartnerGet expected = new PartnerGet(
                putContract.getName(),
                putContract.getTransports(),
                putContract.getStatus(),
                putContract.isPrepaid()
        );

        try {
            PartnerGet actual = partnerFabric.updatePartnerById(findingId, putContract);
            JsonContractWorker.assertContract(actual, expected);
        } finally {
            partnerFabric.deleteById(findingId);
        }
    }

    // Отрицательные

    @DisplayName("Контракт: фильтрация по несуществующему параметру")
    @Test
    void contractFilterByUnexpectedParameter() {
        assertEquals(List.of(), partnerFabric.getAllByFilter("?unexpected=parameter"));
    }

    @DisplayName("Контракт: фильтрация по несуществующему значению параметра")
    @Test
    void contractFilterByUnexpectedValueOfParameter() {
        assertEquals(List.of(), partnerFabric.getAllByFilter("?transports=MMS"));
    }

    @DisplayName("Контракт: создание клиента без обязательного параметра")
    @Test
    void contractCreateWithoutRequiredParameter() {
        PartnerPost contract = new PartnerPost(
                "CONTRACT_REQUIRED_PARAM",
                null,
                true,
                0
        );

        adminConsoleApiWorker.get()
                .post("/acapi/partners", JsonMapperHolder.convertContractToString(contract))
                .code(400);
    }

    @DisplayName("Контракт: создание клиента с некорректным значением параметра")
    @Test
    void contractCreateIncorrectValueOfParameter() {
        PartnerPost contract = new PartnerPost(
                "CONTRACT_UNEXPECTED_PARAM",
                new PartnerTransport[]{
                        new PartnerTransport(1, true, true, true)
                },
                true,
                2
        );

        adminConsoleApiWorker.get()
                .post("/acapi/partners", JsonMapperHolder.convertContractToString(contract))
                .code(400);
    }

    private PartnerPost[] partnersToCreate() {
        return new PartnerPost[]{
                new PartnerPost.Builder()
                        .withName("CONTRACT_PARTNER_DATA_1")
                        .withPrepaid(true)
                        .withStatus(1)
                        .withTransports(new PartnerTransport[]{
                                new PartnerTransport.Builder().withTransport(Transport.SMS, transportFabric).build()
                        })
                        .build(),
                new PartnerPost.Builder()
                        .withName("CONTRACT_PARTNER_DATA_2")
                        .withPrepaid(true)
                        .withStatus(1)
                        .withTransports(new PartnerTransport[]{
                                new PartnerTransport.Builder().withTransport(Transport.VIBER, transportFabric).build(),
                                new PartnerTransport.Builder().withTransport(Transport.SMS, transportFabric).build()
                        })
                        .build(),
                new PartnerPost.Builder()
                        .withName("CONTRACT_PARTNER_DATA_3")
                        .withPrepaid(true)
                        .withStatus(1)
                        .withTransports(new PartnerTransport[]{
                                new PartnerTransport.Builder().withTransport(Transport.PUSH, transportFabric).build()
                        })
                        .build(),
                new PartnerPost.Builder()
                        .withName("CONTRACT_PARTNER_DATA_4")
                        .withPrepaid(true)
                        .withStatus(1)
                        .withTransports(new PartnerTransport[]{
                                new PartnerTransport.Builder().withTransport(Transport.CALL, transportFabric).build()
                        })
                        .build()
        };
    }

    private void createPartners(PartnerPost[] partnerPosts) {
        for (PartnerPost partnerPost : partnerPosts) {
            partnerFabric.createPartner(partnerPost);
        }
    }

    private void deletePartners(PartnerPost[] partnerPosts) {
        for (PartnerPost partnerPost : partnerPosts) {
            partnerFabric.deleteByName(partnerPost.getName());
        }
    }
}
