package com.xweisoft.wx.family.ui.contact;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.logic.model.ContactItem;
import com.xweisoft.wx.family.service.database.ContactsDBHelper;
import com.xweisoft.wx.family.ui.BaseActivity;
import com.xweisoft.wx.family.ui.contact.adapter.ContactsListAdapter;
import com.xweisoft.wx.family.util.ListUtil;
import com.xweisoft.wx.family.util.LoginUtil;
import com.xweisoft.wx.family.widget.swipe.SwipeRefreshListView;

/**
 * 搜索
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  poorgod
 * @version  [版本号, 2015-7-27]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ContactSearchActivity extends BaseActivity
{
    /**
     * 列表
     */
    private SwipeRefreshListView mListView;
    
    /**
     * 适配器
     */
    private ContactsListAdapter mAdapter;
    
    /**
     * 集合
     */
    private ArrayList<ContactItem> mList = new ArrayList<ContactItem>();
    
    /**
     * 输入框
     */
    private EditText mInputEdit;
    
    /**
     * 通讯录数据库帮助类
     */
    private ContactsDBHelper mDbHelper;
    
    private String isFriend;

	private TextView tvTop;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        initViews();
        bindListener();
    }
    
    @Override
    public void initViews()
    {
       tvTop = (TextView) findViewById(R.id.generalTitleLabel);
       tvTop.setText(R.string.ysh_search);
        mListView = (SwipeRefreshListView) findViewById(R.id.contact_search_listview);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mAdapter = new ContactsListAdapter(mContext, false);
        mAdapter.setList(mList);
        mListView.setAdapter(mAdapter);
        
        mInputEdit = (EditText) findViewById(R.id.contact_search_edittext);
        
        mDbHelper = new ContactsDBHelper(mContext,
                LoginUtil.getUserId(mContext));
    }
    
    @Override
    public void bindListener()
    {
        mInputEdit.addTextChangedListener(new TextWatcher()
        {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count)
            {
                
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after)
            {
                
            }
            
            @Override
            public void afterTextChanged(Editable s)
            {
                if (null != s)
                {
                    mList.clear();
                    ArrayList<ContactItem> list = mDbHelper.queryByContent(s.toString()
                            .trim(),
                            isFriend);
                    if (!ListUtil.isEmpty(list))
                    {
                        mList = list;
                    }
                    else
                    {
                        showToast("暂无查询结果");
                    }
                    mAdapter.setList(mList);
                }
            }
        });
        
        mListView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                position -= mListView.getHeaderViewsCount();
                if (position < 0 || position > mList.size() - 1)
                {
                    return;
                }
                if (null != mList.get(position))
                {
                    Intent intent = new Intent(mContext,
                            ContactInfoActivity.class);
                    intent.putExtra("userId", mList.get(position).userId);
                    intent.putExtra("studentId", mList.get(position).studentId);
                    startActivity(intent);
                }
            }
        });
    }
    
    @Override
    public int getActivityLayout()
    {
        return R.layout.wx_contact_search_activity;
    }
    
}
