package com.blackflagbin.kcommonproject.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.blackflagbin.kcommon.facade.CommonLibrary
import com.blackflagbin.kcommon.http.subscribers.NoProgressObserver
import com.blackflagbin.kcommon.http.subscribers.ObserverCallBack
import com.blackflagbin.kcommonproject.common.entity.net.DataItem
import com.blackflagbin.kcommonproject.mvp.contract.MainPageContract
import com.blackflagbin.kcommonproject.mvp.model.MainPageModel
import com.trello.rxlifecycle2.kotlin.bindToLifecycle

class MainPagePresenter(iMainPageView: MainPageContract.IMainPageView) :
        BasePresenter<MainPageContract.IMainPageModel, MainPageContract.IMainPageView>(iMainPageView),
        MainPageContract.IMainPagePresenter {
    override val model: MainPageContract.IMainPageModel
        get() = MainPageModel()

    override fun initData(dataMap: Map<String, String>?) {
        initData(dataMap, CommonLibrary.instance.startPage)
    }

    override fun initData(dataMap: Map<String, String>?, pageNo: Int) {
        if (pageNo == CommonLibrary.instance.startPage) {
            mView.beforeInitData()
            mModel.getData(
                    dataMap!!["type"].toString(),
                    pageNo,
                    CommonLibrary.instance.pageSize).bindToLifecycle(mLifecycleProvider).subscribeWith(
                    NoProgressObserver<List<DataItem>>(mView,
                            object : ObserverCallBack<List<DataItem>> {
                                override fun onNext(t: List<DataItem>) {
                                    mView.showSuccessView(t)
                                    mView.dismissLoading()
                                }

                                override fun onError(e: Throwable) {
                                    mView.showErrorView("")
                                    mView.dismissLoading()
                                }
                            }))
        } else {
            mModel.getData(
                    dataMap!!["type"].toString(),
                    pageNo,
                    CommonLibrary.instance.pageSize).bindToLifecycle(mLifecycleProvider).subscribeWith(
                    NoProgressObserver<List<DataItem>>(mView,mIsLoadMore = true))
        }
    }
}
