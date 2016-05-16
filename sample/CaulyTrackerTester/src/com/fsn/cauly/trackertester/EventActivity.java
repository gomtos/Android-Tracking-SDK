package com.fsn.cauly.trackertester;

import java.util.Locale;

import org.json.JSONObject;

import com.fsn.cauly.tracker.CaulyTrackerBuilder;
import com.fsn.cauly.tracker.CaulyTrackerContentViewEvent;
import com.fsn.cauly.tracker.CaulyTrackerPurchaseEvent;
import com.fsn.cauly.tracker.Logger;
import com.fsn.cauly.tracker.Logger.LogLevel;
import com.fsn.cauly.tracker.Product;
import com.fsn.cauly.tracker.TrackerConst;
import com.fsn.cauly.tracker.exception.CaulyException;
import com.fsn.cauly.volley.RequestQueue;
import com.fsn.cauly.volley.VolleyError;
import com.fsn.cauly.volley.toolbox.ImageLoader;
import com.fsn.cauly.volley.toolbox.ImageLoader.ImageContainer;
import com.fsn.cauly.volley.toolbox.ImageLoader.ImageListener;
import com.fsn.cauly.volley.toolbox.Volley;

import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EventActivity extends AppCompatActivity implements ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		int viewPagerItem = 0;
		Uri data = this.getIntent().getData();
		if (data != null && data.isHierarchical()) {
			String uri = this.getIntent().getDataString();

			String path = data.getPath();
			System.out.println(path);
			if (path.equals("/product")) {
				viewPagerItem = 1;
			} else {
				viewPagerItem = 0;
			}
			try {
				CaulyTrackerBuilder.getTrackerInstance().traceDeepLink(uri);
			} catch (CaulyException e) {
				e.printStackTrace();
			}

		}

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}

		mViewPager.setCurrentItem(viewPagerItem);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.purchase_section).toUpperCase(l);
			case 1:
				return getString(R.string.product_section).toUpperCase(l);

			}
			return null;
		}

	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements OnClickListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		private Button btnPurchaseEvent;

		private EditText editTextOrderId;
		private EditText editTextOrderPrice;

		private EditText editTextProduct1Id;
		private EditText editTextProduct1Price;
		private EditText editTextProduct1Quantity;

		private EditText editTextProduct2Id;
		private EditText editTextProduct2Price;
		private EditText editTextProduct2Quantity;

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
			View rootView = null;
			if (sectionNumber == 1) {

				rootView = inflater.inflate(R.layout.fragment_event, container, false);

				btnPurchaseEvent = (Button) rootView.findViewById(R.id.btnPurchaseEvent);
				btnPurchaseEvent.setOnClickListener(this);

				editTextOrderId = (EditText) rootView.findViewById(R.id.editTextOrderId);
				editTextOrderPrice = (EditText) rootView.findViewById(R.id.editTextOrderPrice);

				editTextOrderId.setText("order_20160427_11");
				editTextOrderPrice.setText("");

				editTextProduct1Id = (EditText) rootView.findViewById(R.id.editTextProductId1);
				editTextProduct2Id = (EditText) rootView.findViewById(R.id.editTextProductId2);

				editTextProduct1Price = (EditText) rootView.findViewById(R.id.editTextProductPrice1);
				editTextProduct2Price = (EditText) rootView.findViewById(R.id.editTextProductPrice2);

				editTextProduct1Quantity = (EditText) rootView.findViewById(R.id.editTextProductQuantity1);
				editTextProduct2Quantity = (EditText) rootView.findViewById(R.id.editTextProductQuantity2);

				editTextProduct1Id.setText("product1");
				editTextProduct1Price.setText("10000");
				editTextProduct1Quantity.setText("2");

				editTextProduct2Id.setText("product2");
				editTextProduct2Price.setText("20000");
				editTextProduct2Quantity.setText("1");
			} else if (sectionNumber == 2) {
				rootView = inflater.inflate(R.layout.fragment_product, container, false);
				Button sendBtn = (Button) rootView.findViewById(R.id.btnSendProductEvent);

				final ImageView productImage = (ImageView) rootView.findViewById(R.id.productImage);

				TextView textViewState = (TextView) rootView.findViewById(R.id.itemState);
				TextView textViewCategory = (TextView) rootView.findViewById(R.id.itemCategory);
				TextView textViewItemName = (TextView) rootView.findViewById(R.id.itemName);
				TextView textViewItemOriginalPrice = (TextView) rootView.findViewById(R.id.itemOriginalPrice);
				TextView textViewItemSalePrice = (TextView) rootView.findViewById(R.id.itemSalePrice);
				TextView textViewItemStock = (TextView) rootView.findViewById(R.id.itemStock);
				TextView textViewDescription = (TextView) rootView.findViewById(R.id.itemDescription);

				final String itemId = "p20160510_test_1";
				final String itemName = "[오늘의 특가] 카울리 반창고!";
				final String itemImage = "https://www.cauly.net/images/logo_cauly_main.png";
				final String itemUrl = "caulytrackertest://caulytracker.com/product?item_id=p20160510_test_1";
				final String originalPrice = "24000";
				final String salePrice = "18000";
				final String category1 = "생활물품";
				final String category2 = "구급";

				final String category3 = "";
				final String category4 = "";
				final String category5 = "";
				final String regDate = "";
				final String updateDate = "";
				final String expireDate = "";
				final String stock = "10";
				final String state = "available";
				final String description = " 한번 사용하면 멈출 수 없는 쫄깃함 !";
				final String extraImage = "";
				final String locale = "KRW";

				textViewState.setText(state.equals("available") ? "판매중" : "일시품절");
				textViewCategory.setText(category1 + " > " + category2);
				textViewItemName.setText(itemName);
				textViewItemOriginalPrice.setText(locale + " " + originalPrice);
				textViewItemOriginalPrice
						.setPaintFlags(textViewItemOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
				textViewItemSalePrice.setText(locale + " " + salePrice);

				textViewItemStock.setText("재고" + stock + "개");
				textViewDescription.setText(description);

				RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
				ImageLoader mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(Integer.MAX_VALUE));

				mImageLoader.get(itemImage, new ImageListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

					}

					@Override
					public void onResponse(ImageContainer response, boolean isImmediate) {
						productImage.setImageBitmap(response.getBitmap());

					}
				});

				sendBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						CaulyTrackerContentViewEvent caulyTrackerContentViewEvent = new CaulyTrackerContentViewEvent(
								itemId);

						caulyTrackerContentViewEvent.setItemImage(itemImage);
						caulyTrackerContentViewEvent.setItemName(itemName);
						caulyTrackerContentViewEvent.setItemUrl(itemUrl);
						caulyTrackerContentViewEvent.setOriginalPrice(originalPrice);
						caulyTrackerContentViewEvent.setSalePrice(salePrice);
						caulyTrackerContentViewEvent.setCategory1(category1);
						caulyTrackerContentViewEvent.setCategory2(category2);

						// Optional info.
						CaulyTrackerContentViewEvent.ContentDetail detail = new CaulyTrackerContentViewEvent.ContentDetail();

						detail.setCategory3(category3);
						detail.setCategory4(category4);
						detail.setCategory5(category5);
						detail.setRegDate(regDate);
						detail.setUpdateDate(updateDate);
						detail.setExpireDate(expireDate);
						detail.setStock(stock);
						detail.setState(state);
						detail.setDescription(description);
						detail.setExtraImage(extraImage);
						detail.setLocale(locale);

						caulyTrackerContentViewEvent.setDetail(detail);
						JSONObject jsonObject = caulyTrackerContentViewEvent.toJson();
						Logger.writeLog(LogLevel.Debug, "Product : " + jsonObject);

						try {
							CaulyTrackerBuilder.getTrackerInstance().trackEvent(caulyTrackerContentViewEvent);
						} catch (CaulyException e) {
							e.printStackTrace();
						}
					}
				});

			}
			return rootView;
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btnPurchaseEvent) {

				String productId = editTextProduct1Id.getText().toString();
				String productPrice = editTextProduct1Price.getText().toString();
				String productQuantity = editTextProduct1Quantity.getText().toString();

				CaulyTrackerPurchaseEvent purchaseEvent = new CaulyTrackerPurchaseEvent();

				Product product = new Product(productId, productPrice, productQuantity);

				String product2Id = editTextProduct2Id.getText().toString();
				String product2Price = editTextProduct2Price.getText().toString();
				String product2Quantity = editTextProduct2Quantity.getText().toString();

				int orderPrice = Integer.parseInt(productPrice) * Integer.parseInt(productQuantity);
				orderPrice += Integer.parseInt(product2Price) * Integer.parseInt(product2Quantity);

				editTextOrderPrice.setText("" + orderPrice);
				Product product2 = new Product(product2Id, product2Price, product2Quantity);
				try {
					purchaseEvent.setOrderId(editTextOrderId.getText().toString());
					purchaseEvent.setOrderPrice(editTextOrderPrice.getText().toString());
					purchaseEvent.addProuduct(product);
					purchaseEvent.addProuduct(product2);
					purchaseEvent.setCurrencyCode(TrackerConst.CURRENCY_KRW);

					CaulyTrackerBuilder.getTrackerInstance().trackEvent(purchaseEvent);

				} catch (CaulyException e) {
					Logger.writeLog(LogLevel.Error, e.getMessage());
				}

			}
		}
	}

}
