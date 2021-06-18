package com.luhong.locwithlibrary.dialogfragment.library;



import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

public class Ext
{
    /**
     * 快捷对ViewModel进行实例化
     *
     * @param klass viewmodel的类
     * @param <T>
     * @return 实例
     */
    public static <T extends ViewModel> T create(ViewModelStoreOwner owner, Class<T> klass)
    {
        return new ViewModelProvider(owner,new ViewModelProvider.NewInstanceFactory()).get(klass);
    }
}
