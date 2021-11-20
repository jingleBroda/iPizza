package com.example.ipizza.data.repositoriesImpl

import android.util.Log
import com.example.ipizza.data.dataBase.PizzaDao
import com.example.ipizza.data.dataBase.PizzaDataBase
import com.example.ipizza.domain.model.PizzaModel
import com.example.ipizza.data.retrofit.RetrofitServices
import com.example.ipizza.domain.model.CartModel
import com.example.ipizza.domain.repositories.DomainRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable

class PizzaRepositoriesImpl(
    private val retroInstance: RetrofitServices,
    private val dbDao: PizzaDao
    ) : DomainRepository {

    //1
    override fun makeApiPizza(): Single<List<PizzaModel>> {

        return  retroInstance.getPizzaFullInfo()

    }

    override fun insertDataRoom(pizza: PizzaModel): Completable {
        return dbDao.insertPizza(pizza)
    }


    override fun getAllPizzaRoom(): Single<List<PizzaModel>> {
        return dbDao.getPizza()
    }
    //
    //2
    //?????????????????
    override fun searchSpecificPizza(id: Int): Single<PizzaModel> {
        return dbDao.getSpecificPizza(id)
    }

    override fun searchSpecificOrderPizza(name: String): Single<CartModel> {
        return dbDao.getSpecificOrferPizza(name)
    }

    override fun insertOrderPizza(order: CartModel): Completable {
        return dbDao.insertOrderPizza(order)
    }
    //
    //3
    override fun updateOrder(order: CartModel): Completable {
        return dbDao.updateOrder(order)
    }

    override fun deleteOrder(): Completable {
        return dbDao.deleteOrder()
    }

    override fun getOrderDataRoom(): Single<List<CartModel>> {
        return dbDao.getOrder()
    }
    //

}