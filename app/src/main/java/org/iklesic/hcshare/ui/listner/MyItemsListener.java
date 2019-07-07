package org.iklesic.hcshare.ui.listner;

import org.iklesic.hcshare.model.ShareItem;

public interface MyItemsListener {

    void onItemClicked(ShareItem shareItem);

    void onItemLongClicked(ShareItem shareItem);

}
