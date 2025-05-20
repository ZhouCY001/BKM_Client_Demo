20/05/2025
Update:
Add a new int param 'autoSelectACQ' in AIDL method: String setParams(String jsonData);
if 'autoSelectACQ' ==0 , disable the auto ACQ , else enable the auto ACQ.
If 'autoSelectACQ' is not provided, the Auto ACQ function is enabled by default.


28/2/2025
Update:
Add the 'getBinBlackList' to get the DisableCardBin list from the payment app.
Change the 'setDisableCardBins' logic, 
original-> when terminal download the cardBins from host , remove the bins which in the DisableCardBinsTable
new -> do not remove the bins again, when terminal get the card pan it will auto check:
    1.If the cardPan is in the DisableCardBinTable, it will show 'Bu banka kartı kara listeye alınmış ve devre dışı bırakılmıştır!' and cancel the transaction.
    2.If the cardPan is not in the DisableCardBinTable, execute the normal process.



17/12/2024
Update:
Add 'setDisableCardBins' to set the card bins which need to be disabled




10/10/2024
Update:
Add Pre-Auth/Auth Cancel / Auth Close




18/07/2024
Update:
Add 'printReceipt' tag to control whether Q2/Q2P prints receipt


