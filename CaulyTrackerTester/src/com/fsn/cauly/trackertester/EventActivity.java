package com.fsn.cauly.trackertester;

import java.util.Locale;

import com.fsn.cauly.tracker.CaulyTrackerBuilder;
import com.fsn.cauly.tracker.CaulyTrackerPurchaseEvent;
import com.fsn.cauly.tracker.Logger;
import com.fsn.cauly.tracker.Logger.LogLevel;
import com.fsn.cauly.tracker.Product;
import com.fsn.cauly.tracker.TrackerConst;
import com.fsn.cauly.tracker.exception.CaulyException;

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

public class EventActivity extends AppCompatActivity implements ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		Uri data = this.getIntent().getData();
		if (data != null && data.isHierarchical()) {
			String uri = this.getIntent().getDataString();

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
			return 1;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.purchase_section).toUpperCase(l);

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

		private EditText editTextProductId;
		private EditText editTextProductPrice;
		private EditText editTextProductQuantity;

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
			View rootView = inflater.inflate(R.layout.fragment_event, container, false);
			btnPurchaseEvent = (Button) rootView.findViewById(R.id.btnPurchaseEvent);
			btnPurchaseEvent.setOnClickListener(this);

			editTextProductId = (EditText) rootView.findViewById(R.id.editTextProductName);
			editTextProductPrice = (EditText) rootView.findViewById(R.id.editTextUnitPrice);
			editTextProductQuantity = (EditText) rootView.findViewById(R.id.editTextQuantity);

			editTextProductId.setText("TestItem");
			editTextProductPrice.setText("1.28");
			editTextProductQuantity.setText("2");

			return rootView;
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btnPurchaseEvent) {

				String productId = editTextProductId.getText().toString();
				String productPrice = editTextProductPrice.getText().toString();
				String productQuantity = editTextProductQuantity.getText().toString();

				CaulyTrackerPurchaseEvent purchaseEvent = new CaulyTrackerPurchaseEvent();

				Product product = new Product(productId, productPrice, productQuantity);
				try {
					purchaseEvent.setOrderId("test_order_0001");
					purchaseEvent.setOrderPrice("100,000");
					purchaseEvent.addProuduct(product);
					purchaseEvent.setCurrencyCode(TrackerConst.CURRENCY_KRW);

					CaulyTrackerBuilder.getTrackerInstance().trackEvent(purchaseEvent);

				} catch (CaulyException e) {
					Logger.writeLog(LogLevel.Error, e.getMessage());
				}

			}
		}
	}

}
