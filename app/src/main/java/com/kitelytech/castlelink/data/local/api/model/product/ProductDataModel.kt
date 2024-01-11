package com.kitelytech.castlelink.data.local.api.model.product

class ProductDataModel(
    val multiLang: List<MultiLang>,
    val controllers: List<Controller>,
    val lookUpList: List<LookUp>,
    val settingsGroup: List<SettingGroup>,
    val connectHelpList: List<ConnectHelp>,
    val databaseInfo: DatabaseInfo
) {
}