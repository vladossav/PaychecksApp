package ru.savenkov.paychecksapp

import ru.savenkov.paychecksapp.model.network.data.CheckItem

val data: CheckItem = CheckItem(
    data = CheckItem.Data(
        CheckItem.Data.JsonObj(
            operationType = 1,
            dateTime= "2022-09-12T17:53:00",
            fiscalDocumentNumber= 15488,
            fiscalDriveNumber = "9960440502833901",
            fiscalSign="836979144",
            items = listOf(
                CheckItem.Data.JsonObj.Item(
                    name= "Пакет белый без логотипа майка пнд(28+16)*50, 15 мкм/2000",
                    price=499, quantity=1f, sum=499
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Молоко Томское Молоко Отборное 900г пл/бут/6/БЗМЖ",
                    price=5999, quantity=1f, sum=5999
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Молоко Томское Молоко Отборное 900г пл/бут/6/БЗМЖ",
                    price=5999, quantity=1f, sum=5999
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Масло подсолнечное СЛОБОДА рафинированное 1л/15",
                    price=11999, quantity=1f, sum=11999
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Майонез СЛОБОДА 400мл Оливковый 67% д/пак/24",
                    price=12999, quantity=1f, sum=12999
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Сметана Хороший Выбор 15% 350г пл/уп/6/БЗМЖ",
                    price=6499, quantity=1f, sum=6499
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Сыр творожный Хохланд Сливочный 60% 220г ванна/6/БЗМЖ",
                    price=18999, quantity=1f, sum=18999
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Карбонат ИМК По-Ишимски в/к 300г термоусад.пакет/8",
                    price=15999, quantity=1f, sum=15999
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Масло сливочное Лебедевская агрофирма Крестьянское 72,5% 180г фольга/32",
                    price=13489, quantity=1f, sum=13489
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Творог Простоквашино 2% 200г/6 клинок/БЗМЖ",
                    price=9999, quantity=1f, sum=9999
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Макароны РОЛЛТОН Рожки 400г/16",
                    price=7499, quantity=1f, sum=7499
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Тараллини Nina Farina классические 180г/24",
                    price=4299, quantity=1f, sum=4299
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Хлопья ГУДВИЛЛ Гречневые 400г м/у/18",
                    price=9999, quantity=1f, sum=9999
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Биойогурт Активиа 130г Киви-мюсли 3% пл/уп/12/БЗМЖ",
                    price=3999, quantity=1f, sum=3999
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Биойогурт Активиа 130г Киви-мюсли 3% пл/уп/12/БЗМЖ",
                    price=3999, quantity=1f, sum=3999
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Хлеб бездрожжевой фирменный(с тыквенными семечками), 0,230кг/ФК/СП",
                    price=4999, quantity=1f, sum=4999
                ),
                CheckItem.Data.JsonObj.Item(
                    name= "Хлеб Отрубной нарезка 400г/1/Инской",
                    price=4299, quantity=1f, sum=4299
                ),
            ),
            kktRegId="0005513318051218",
            numberKkt="0128001533",
            metadata = CheckItem.Data.JsonObj.Metadata(
                address="630106,Россия,Новосибирская область,город Новосибирск г.о.,,Новосибирск г,,Громова ул,,д. 15,,,,,",
                ofdId="ofd9",
                receiveDate="2022-09-12T10:53:39Z",
                subtype="receipt",
                id=4412072596026958595
            ),
            operator ="Макаренко",
            region="54",
            requestNumber=385,
            retailPlace="Универсам \"Хороший Выбор\"",
            retailPlaceAddress="630106, г. Новосибирск, ул. Громова, 15.",
            shiftNumber=34,
            totalSum=141573,
            user="ООО \"Перспектива\"",
            userInn="7017361618"
        )
    ),
    code = 1
)

/*
val checkItem = data.data.jsonObj

val checkEntity = CheckEntity(0,
    checkItem.dateTime,
    null,
    checkItem.totalSum
)

val checkDetailsEntity = CheckDetailsEntity(
)


val goodsEntity = checkItem.items.map {
    GoodEntity(0, checkEntity.id,it.name,
        it.price, it.quantity, it.sum)
}


val checkAllInfoEntity = CheckAllInfoTuple(
    checkEntity, checkDetailsEntity,
    goodsEntity
)*/
